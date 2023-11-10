package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.repositories.CategoryRepository;
import com.r2s.mobilestore.manufacturer.entities.Manufacturer;
import com.r2s.mobilestore.manufacturer.repositories.ManufacturerRepository;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.entities.Property;
import com.r2s.mobilestore.product.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private Integer length;

    @Override
    public Product createProduct(CreateProductDTO createProductDTO) throws IOException {
        Product product = new Product();

        product.setProductName(createProductDTO.getProductName());
        product.setProductCondition(createProductDTO.getProductCondition());
        product.setDescription(createProductDTO.getDescription());

        //Find name of manufacturer
        List<Manufacturer> manufacturer = manufacturerRepository.findByManufacturerNameContaining(createProductDTO.getManufacturer());
        product.setManufacturer(manufacturer.get(0));

        //Find name of category
        List<Category> category = categoryRepository.findByCategoryNameContaining(createProductDTO.getCategory());
        product.setCategory(category.get(0));

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
//            return saveProduct;
            Integer sizeOfCapacity = createProductDTO.getCapacityList().size();
            Integer sizeOfColor = createProductDTO.getColorList().size();
            Integer sizeOfConfiguration = createProductDTO.getConfigurationList().size();
            Integer sizeOfConnectionSupport = createProductDTO.getConnectionSupportList().size();

            Integer lengthCapacityTemp = sizeOfCapacity == 0 ? 1 : sizeOfCapacity;
            Integer lengthColorTemp = sizeOfColor == 0 ? 1 : sizeOfColor;
            Integer lengthConfigurationTemp = sizeOfConfiguration == 0 ? 1 : sizeOfConfiguration;
            Integer lengthConnectionSupportTemp = sizeOfConnectionSupport == 0 ? 1 : sizeOfConnectionSupport;

            Integer totalLength = lengthCapacityTemp * lengthColorTemp * lengthConfigurationTemp * lengthConnectionSupportTemp;

            ArrayList<Property> propertyList = new ArrayList<>();

            for (Integer i = 0; i < totalLength; i++) {
                Property property = new Property();
                propertyList.add(property);
            }
            if (sizeOfCapacity != 0) {
                Integer step = totalLength / sizeOfCapacity;
                for (Integer i = 0; i < sizeOfCapacity; i++) {
                    if (step * (i + 1) > totalLength) {
                        break;

                    } else {
                        for (Integer j = step * i; j < step * (i + 1); j++) {
                            propertyList.get(j).setCapacity(createProductDTO.getCapacityList().get(i));

                        }
                    }
                }

            }

            if (sizeOfColor != 0) {
                Integer step = totalLength / sizeOfColor;
                for (Integer i = 0; i < sizeOfColor; i++) {
                    if (step * (i + 1) > totalLength) {
                        break;

                    } else {
                        for (Integer j = step * i; j < step * (i + 1); j++) {

                            propertyList.get(j).setColor(createProductDTO.getColorList().get(i));

                        }
                    }

                }

            }

            if (sizeOfConfiguration != 0) {
                Integer step = totalLength / sizeOfConfiguration;
                for (Integer i = 0; i < sizeOfConfiguration; i++) {
                    if (step * (i + 1) > totalLength) {
                        break;

                    } else {
                        for (Integer j = step * i; j < step * (i + 1); j++) {

                            propertyList.get(j).setConfiguration(createProductDTO.getConfigurationList().get(i));

                        }
                    }

                }

            }

            if (sizeOfConnectionSupport != 0) {
                Integer step = totalLength / sizeOfConnectionSupport;
                for (Integer i = 0; i < sizeOfConnectionSupport; i++) {
                    if (step * (i + 1) > totalLength) {
                        break;

                    } else {
                        for (Integer j = step * i; j < step * (i + 1); j++) {

                            propertyList.get(j).setConnectionSupport(createProductDTO.getConnectionSupportList().get(i));

                        }
                    }

                }

            }

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


}
