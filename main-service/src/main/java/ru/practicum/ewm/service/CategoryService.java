package ru.practicum.ewm.service;


import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategory;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(NewCategory newCategory);

    void deleteCategory(Integer catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Integer catId);

    CategoryDto updateCategory(NewCategory newCategory, Integer catId);
}
