package com.example.json.service;

import com.example.json.model.dto.ProductNameAndPriceDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;



    List<ProductNameAndPriceDTO> findAllProductsInRangeOrderByPrice(BigDecimal lower, BigDecimal upper);
}
