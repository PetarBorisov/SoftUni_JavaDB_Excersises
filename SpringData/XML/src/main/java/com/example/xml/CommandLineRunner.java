package com.example.xml;

import com.example.xml.model.dto.*;
import com.example.xml.service.CategoryService;
import com.example.xml.service.ProductService;
import com.example.xml.service.UserService;
import com.example.xml.util.XmlParser;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {
    private static final String RESOURCE_FILE_PATH = "src/main/resources/files/";
    private static final String OUTPUT_FILE_PATH = "out/";
    private static final String CATEGORIES_FILE_NAME = "categories.xml";
    private static final String USER_FILE_NAME = "users.xml";
    private static final String PRODUCT_FILE_NAME = "products.xml";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.xml";
    private static final String OUTPUT_SOLD_PRODUCTS_FILE_NAME = "sold-products.xml";

   private final XmlParser xmlParser;
   private final CategoryService categoryService;
   private final UserService userService;
   private final ProductService productService;
   private final BufferedReader bufferedReader;

    public CommandLineRunner(XmlParser xmlParser, CategoryService categoryService, UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Select exercise;");
        int exNum = Integer.parseInt(bufferedReader.readLine());
        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> usersWithSoldProducts();
        }
    }

    private void usersWithSoldProducts() throws JAXBException {
        UserViewRootDto userViewRootDto =
                userService.findWithMoreThanOneSoldProduct();

        xmlParser.writeToFile(RESOURCE_FILE_PATH + OUTPUT_FILE_PATH + OUTPUT_SOLD_PRODUCTS_FILE_NAME,
                userViewRootDto);
    }

    private void productsInRange() throws JAXBException {
        ProductViewRootDto productViewRootDto = productService
                .findProductInRangeWithoutBuyer();

        xmlParser.writeToFile(RESOURCE_FILE_PATH + OUTPUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME,
                productViewRootDto);
    }

    private void seedData() throws JAXBException, FileNotFoundException {
        if (categoryService.getEntityCount() == 0) {

            CategorySeedRootDTO categorySeedRootDTO = xmlParser.fromFile(RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME,
                    CategorySeedRootDTO.class);

            categoryService.seedCategories(categorySeedRootDTO.getCategories());

        }
        if (userService.getCount() == 0) {
            UserSeedRootDTo userSeedRootDTo = xmlParser
                    .fromFile(RESOURCE_FILE_PATH + USER_FILE_NAME,
                    UserSeedRootDTo.class);


            userService.seedUsers(userSeedRootDTo.getUsers());
        }
        if (productService.getCount() == 0) {
            ProductSeedRootDto productSeedRootDto = xmlParser
                    .fromFile(RESOURCE_FILE_PATH + PRODUCT_FILE_NAME,
                            ProductSeedRootDto.class);

            productService.seedProducts(productSeedRootDto.getProducts());

        }

    }
}
