package com.example.xml.service;

import com.example.xml.model.dto.ProductSeedDto;
import com.example.xml.model.dto.ProductViewRootDto;

import java.util.List;

public interface ProductService {
    long getCount();

    void seedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findProductInRangeWithoutBuyer();
}
