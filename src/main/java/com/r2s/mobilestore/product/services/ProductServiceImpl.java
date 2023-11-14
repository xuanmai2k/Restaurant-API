package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.repositories.CategoryRepository;
import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import com.r2s.mobilestore.manufacturer.repositories.ManufacturerRepository;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.dtos.CreatePropertyDetailDTO;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.entities.Property;
import com.r2s.mobilestore.product.repositories.ProductRepository;
import com.r2s.mobilestore.product.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    /**
     * @value random characters
     */
    @Value("${ALLOWED_CHARACTERS}")
    private String allowedCharacters;

    /**
     * @value path of saving images
     */
    @Value("${LOG_PATH}")
    private String folderPath;

    /**
     * @value random characters
     */
    @Value("${LENGTH_OF_PRODUCT_CODE}")
    private Integer LENGTH_OF_PRODUCT_CODE;

    @Override
    public Page<Product> getAllProducts(PageDTO pageDTO) {
        return productRepository.findAll(PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findProductsById(id);
    }

    @Override
    public ArrayList<Property> createProduct(CreateProductDTO createProductDTO) throws IOException {
        Product product = new Product();

        product.setProductName(createProductDTO.getProductName());
        product.setProductCondition(createProductDTO.getProductCondition());
        product.setDescription(createProductDTO.getDescription());

        //Find id of manufacturer
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(createProductDTO.getManufacturer());
        product.setManufacturer(manufacturer.get());

        //Find id of category
        Optional<Category> category = categoryRepository.findById(createProductDTO.getCategory());
        product.setCategory(category.get());

        //rename image
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile imageFile : createProductDTO.getImages()) {
            String imageUrl = generateUniqueFileName(imageFile);
            imageUrls.add(imageUrl);
        }
        product.setImages(imageUrls);

        Product saveProduct = productRepository.save(product);

        //Save successfully
        if (saveProduct != null) {
            //Save images into folder
            for (int i = 0; i < saveProduct.getImages().size(); i++) {
                String item = saveProduct.getImages().get(i);
                MultipartFile imageFile = createProductDTO.getImages().get(i);
                String fullNamePath = folderPath + item;
                saveImage(imageFile, fullNamePath);
            }

            //save Property
            ArrayList<Property> propertyArrayList = createProperty(createProductDTO, saveProduct);
            return propertyArrayList;
        }
        return null;
    }

    public String generateUniqueFileName(MultipartFile image) {
        //rename of image
        String originalFileName = StringUtils.cleanPath(image.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // new name = time + number random
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomString = UUID.randomUUID().toString().replace("-", "");
        String FileName = timestamp + "_" + randomString + fileExtension;

        //each month, create new folder which contains images
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
        String folderName = dateFormat.format(currentDate);

        String nameFileAndNameFolder = folderName + "/" + FileName;
        return nameFileAndNameFolder;
    }

    public String saveImage(MultipartFile image, String fullNamePath) throws IOException {

        //check existing folder
        File folder = new File(fullNamePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //save with path
        Path imagePath = Paths.get(fullNamePath);
        Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        return fullNamePath;
    }

    private ArrayList<Property> createProperty(CreateProductDTO createProductDTO, Product product) {

        List<String> temp = new ArrayList<>();
        temp.add(null);

        //check present
        if (createProductDTO.getCapacityList().size() == 0) {
            createProductDTO.setCapacityList(temp);
        }
        if (createProductDTO.getColorList().size() == 0) {
            createProductDTO.setColorList(temp);
        }
        if (createProductDTO.getConfigurationList().size() == 0) {
            createProductDTO.setConfigurationList(temp);
        }
        if (createProductDTO.getConnectionSupportList().size() == 0) {
            createProductDTO.setConnectionSupportList(temp);
        }

        //size of capacity, color, configuration, connectionSupport
        Integer sizeOfCapacity = createProductDTO.getCapacityList().size();
        Integer sizeOfColor = createProductDTO.getColorList().size();
        Integer sizeOfConfiguration = createProductDTO.getConfigurationList().size();
        Integer sizeOfConnectionSupport = createProductDTO.getConnectionSupportList().size();

        ArrayList<Property> propertyList = new ArrayList<>();

        //save property combination
        for (Integer i = 0; i < sizeOfCapacity; i++) {
            for (Integer j = 0; j < sizeOfColor; j++) {
                for (Integer x = 0; x < sizeOfConfiguration; x++) {
                    for (Integer y = 0; y < sizeOfConnectionSupport; y++) {
                        Property property = new Property();
                        property.setCapacity(createProductDTO.getCapacityList().get(i));
                        property.setColor(createProductDTO.getColorList().get(j));
                        property.setConfiguration(createProductDTO.getConfigurationList().get(x));
                        property.setConnectionSupport(createProductDTO.getConnectionSupportList().get(y));

                        //id of product
                        property.setProduct(product);

                        //random product code
                        property.setProductCode(getRandomProductCode(LENGTH_OF_PRODUCT_CODE));

                        Property savedItem = propertyRepository.save(property);
                        propertyList.add(savedItem);
                    }
                }
            }
        }
        return propertyList;
    }

    @Override
    public void updateProperty(Property property) {
        Optional<Property> updateProperty = propertyRepository.findById(property.getId());

        Property _property = updateProperty.get();
        _property.setCapacity(property.getCapacity());
        _property.setColor(property.getColor());
        _property.setConfiguration(property.getConfiguration());
        _property.setConnectionSupport(property.getConnectionSupport());
        _property.setCapitalPrice(property.getCapitalPrice());
        _property.setPrice(property.getPrice());
        _property.setPreferentialPrice(property.getPreferentialPrice());
        _property.setQuantity(property.getQuantity());

        propertyRepository.save(_property);
    }

    @Override
    public String getRandomProductCode(int length) {
        String productCode;
        //create product code
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        do {
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(allowedCharacters.length());
                sb.append(allowedCharacters.charAt(randomIndex));
            }
            productCode = sb.toString();
        } while (existProductCode(productCode)); // check product code

        return productCode;
    }


    /**
     * This method is used to check existing product code
     *
     * @param productCode This is product code
     */
    public Boolean existProductCode(String productCode) {
        return propertyRepository.existsByProductCode(productCode);
    }

    public void deleteImage(String imageUrl) {
        //get name of folder and image
        String[] parts = imageUrl.split("/");
        String folderName = parts[0];
        String imageName = parts[1];

        //path for deleting image file in folder
        String filePath = folderPath + folderName + "/" + imageName;

        // Delete the image file
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }
    }


}
