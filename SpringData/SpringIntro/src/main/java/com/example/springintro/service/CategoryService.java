package com.example.springintro.service;

import com.example.springintro.Entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService{

       void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
