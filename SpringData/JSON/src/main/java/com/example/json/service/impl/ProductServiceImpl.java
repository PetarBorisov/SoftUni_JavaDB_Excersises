package com.example.json.service.impl;

import com.example.json.constants.GlobalConstants;
import com.example.json.model.dto.ProductNameAndPriceDTO;
import com.example.json.model.dto.ProductSeedDTO;
import com.example.json.model.entity.Product;
import com.example.json.repository.ProductRepository;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCTS_FILE_NAME = "products.json";
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public ProductServiceImpl(UserService userService, CategoryService categoryService, ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedProducts() throws IOException {
        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files
                .readString(Path.of(GlobalConstants.RESOURCE_FILE_PATH + PRODUCTS_FILE_NAME));

        ProductSeedDTO[] productSeedDTOS = gson
                .fromJson(fileContent, ProductSeedDTO[].class);

        Arrays.stream(productSeedDTOS)
                .filter(validationUtil::isValid)
                .map(productSeedDTO -> {
                    Product product = modelMapper.map(productSeedDTO,Product.class);
                    product.setSeller(userService.findRandomUser());

                    if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) >  0) {
                        product.setBuyer(userService.findRandomUser());
                    }
                    product.setCategories(categoryService.findRandomCategories());

                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDTO> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper) {
        return productRepository
                .findAllByPriceBetweenAndBuyerIsNullOrderByPriceDesc(lower,upper)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDTO productNameAndPriceDTO = modelMapper
                            .map(product,ProductNameAndPriceDTO.class);

                    productNameAndPriceDTO.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                           return productNameAndPriceDTO;
                })
                .collect(Collectors.toList());
    }
}
