package com.example.xml.service;

import com.example.xml.model.dto.CategorySeedDTO;
import com.example.xml.model.entity.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories(List<CategorySeedDTO> categories);

    long getEntityCount();

    Set<Category> getRandomCategories();

}
