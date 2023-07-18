package com.example.json;

import com.example.json.model.dto.CategoryByProductsDTO;
import com.example.json.model.dto.ProductNameAndPriceDTO;
import com.example.json.model.dto.UserSoldDTO;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUTPUT_PATH = "src/main/resources/files/out/";
    private static final String PRODUCT_IN_RANGE_FAIL_NAME = "products-in-range.json";
    private static final String USERS_AND_SOLD_PRODUCTS = "users-sold-products.json";
    public static final String CATEGORY_BY_PRODUCTS = "categories-by-products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;

        bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    }


    @Override
    public void run(String... args) throws Exception {

        seedData();
        System.out.println("Enter exercise:");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByProductsCount();
        }
    }

    private void categoriesByProductsCount() throws IOException {
     List<CategoryByProductsDTO> categoryByProductsDTOList = categoryService
             .findAllCategoriesByProductsCount();

     String content = gson
             .toJson(categoryByProductsDTOList);

     writeToFile(OUTPUT_PATH + CATEGORY_BY_PRODUCTS,content);
    }

    private void soldProducts() throws IOException {
       List<UserSoldDTO> userSoldDTOS = userService
               .findAllUsersWithMoreThenOneSoldProducts();

       String content = gson
               .toJson(userSoldDTOS);

       writeToFile(OUTPUT_PATH + USERS_AND_SOLD_PRODUCTS,content);

    }


    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDTO> productDTOS = productService
                .findAllProductsInRangeOrderByPrice(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productDTOS);
        writeToFile(OUTPUT_PATH + PRODUCT_IN_RANGE_FAIL_NAME,content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files
                .write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();

    }
}
