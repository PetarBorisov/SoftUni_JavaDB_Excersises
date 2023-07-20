package com.example.json.service.impl;

import com.example.json.constants.GlobalConstants;
import com.example.json.model.dto.CategoryByProductsDTO;
import com.example.json.model.dto.CategorySeedDTO;
import com.example.json.model.entity.Category;
import com.example.json.repository.CategoryRepository;
import com.example.json.service.CategoryService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORIES_FILE_NAME = "categories.json";
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }


    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0){
            return;
        }

    String fileContent = Files.readString(Path.of(GlobalConstants.RESOURCE_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDTO[] categorySeedDTOS = gson.fromJson(fileContent, CategorySeedDTO[].class);


        Arrays.stream(categorySeedDTOS)
                .filter(validationUtil::isValid)
                .map(categorySeedDTO -> modelMapper.map(categorySeedDTO, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomCategories() {
        Set<Category> categorySet = new HashSet<>();
        int catCount = ThreadLocalRandom.current().nextInt(1,3);
        long totalCategoriesCount = categoryRepository.count();

        for (int i = 0; i < catCount; i++) {
            long randomId = ThreadLocalRandom
                    .current().nextLong(1,totalCategoriesCount + 1);

            categorySet.add(categoryRepository.findById(randomId)
                    .orElse(null));

        }


        return categorySet;
    }

    @Override
    public List<CategoryByProductsDTO> findAllCategoriesByProductsCount() {
        return categoryRepository.CategoryByProductSummary()
                .stream()
                .map(Category-> modelMapper.map(Category,CategoryByProductsDTO.class))
                .collect(Collectors.toList());
    }
}
