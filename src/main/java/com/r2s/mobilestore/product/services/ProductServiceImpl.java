package com.r2s.mobilestore.product.services;

import com.r2s.mobilestore.category.entities.Category;
import com.r2s.mobilestore.category.repositories.CategoryRepository;
import com.r2s.mobilestore.dtos.PageDTO;
import com.r2s.mobilestore.product.dtos.CreateProductDTO;
import com.r2s.mobilestore.product.dtos.SearchProductDTO;
import com.r2s.mobilestore.product.entities.Manufacturer;
import com.r2s.mobilestore.product.entities.Product;
import com.r2s.mobilestore.product.repositories.ManufacturerRepository;
import com.r2s.mobilestore.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * This method is used to list all products
     *
     * @param pageDTO This is a page
     * @return list of products
     */
    @Override
    public Page<Product> getAllProducts(PageDTO pageDTO) {
        return productRepository.findAll(PageRequest.of(pageDTO.getPageNumber(), pageDTO.getPageSize()));
    }

    /**
     * This method is used to get a product base on id
     *
     * @param id This is product id
     * @return product base on id
     */
    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findProductsById(id);
    }

    /**
     * This method is used to create a product
     *
     * @param createProductDTO This is a product
     * @return new product
     */
    @Override
    public Product createProduct(CreateProductDTO createProductDTO) throws IOException {

        Product product = new Product();

        product.setProductCode(getRandomProductCode(length));
        product.setName(createProductDTO.getName());
        product.setPrice(createProductDTO.getPrice());
        product.setDiscountPrice(createProductDTO.getDiscountPrice());
        product.setQuantity(createProductDTO.getQuantity());
        product.setDescription(createProductDTO.getDescription());
        product.setMemoryTypes(createProductDTO.getMemoryTypes());
        product.setColor(createProductDTO.getColor());
        product.setProductCondition(createProductDTO.getProductCondition());

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
            return saveProduct;
        }

        return null;
    }

    /**
     * This method is used to update a product
     *
     * @param createProductDTO This is a product
     * @param id               This is a product id
     * @return updated product
     */
    @Override
    public Product updateProduct(CreateProductDTO createProductDTO, Long id) throws IOException {
        //find product by id
        Optional<Product> product = productRepository.findProductsById(id);

        //check existing product
        if (product.isPresent()) {
            Product updateProduct = product.get();

            //unlink image in folder
            for (String item : product.get().getImages()) {
                deleteImage(item);
            }

            //save Product
            updateProduct.setProductCode(product.get().getProductCode());
            updateProduct.setName(createProductDTO.getName());
            updateProduct.setPrice(createProductDTO.getPrice());
            updateProduct.setDiscountPrice(createProductDTO.getDiscountPrice());
            updateProduct.setQuantity(createProductDTO.getQuantity());
            updateProduct.setDescription(createProductDTO.getDescription());
            updateProduct.setMemoryTypes(createProductDTO.getMemoryTypes());
            updateProduct.setColor(createProductDTO.getColor());
            updateProduct.setProductCondition(createProductDTO.getProductCondition());

            //Find name of manufacturer
            List<Manufacturer> manufacturer = manufacturerRepository.findByManufacturerNameContaining(createProductDTO.getManufacturer());
            updateProduct.setManufacturer(manufacturer.get(0));

            //Find name of category
            List<Category> category = categoryRepository.findByCategoryNameContaining(createProductDTO.getCategory());
            updateProduct.setCategory(category.get(0));

            //rename image
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : createProductDTO.getImages()) {
                String imageUrl = generateUniqueFileName(imageFile);
                imageUrls.add(imageUrl);
            }
            updateProduct.setImages(imageUrls);

            Product saveProduct = productRepository.save(updateProduct);

            //Save successfully
            if (saveProduct != null) {
                for (int i = 0; i < saveProduct.getImages().size(); i++) {
                    String item = saveProduct.getImages().get(i);
                    MultipartFile imageFile = createProductDTO.getImages().get(i);

                    String fullNamePath = folderPath + item;
                    saveImage(imageFile, fullNamePath);
                }
                return saveProduct;
            }
            return null;
        }
        return null;
    }

    /**
     * This method is used to save image into folder
     *
     * @param image        This is image file of product
     * @param fullNamePath This is a path of folder
     * @return new name of image
     */
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

    /**
     * This method is used to generate the new name of image
     *
     * @param image This is image of product
     * @return new name of image
     */
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

    /**
     * This method is used to delete a product
     *
     * @param product This is product
     */
    @Override
    public void deleteProduct(Product product) throws IOException {
        //delete image in folder
        for (String item : product.getImages()) {
            deleteImage(item);
        }
        productRepository.delete(product);
    }

    /**
     * This method is used to delete image of product
     *
     * @param imageUrl This is url image of product
     */
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

    /**
     * This method is used to random product code
     *
     * @param length This is length of product code
     */
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
        return productRepository.existsByProductCode(productCode);
    }

    /**
     * This method is used to search product
     *
     * @param searchProductDTO There are keyword, category and manufacturer
     * @return list of products
     */
    @Override
    public Page<Product> search(SearchProductDTO searchProductDTO) {
        return productRepository.searchProduct(
                searchProductDTO.getKeyword(),
                searchProductDTO.getManufacturer(),
                searchProductDTO.getCategory(),
                PageRequest.of(searchProductDTO.getPageDTO().getPageNumber(), searchProductDTO.getPageDTO().getPageSize()));
    }
}
