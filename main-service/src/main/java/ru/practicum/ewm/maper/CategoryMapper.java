package ru.practicum.ewm.maper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategory;
import ru.practicum.ewm.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryMapper {

    public Category fromNewCategoryToEntity(NewCategory newCategory) {
        Category category = new Category();

        category.setName(newCategory.getName());

        return category;
    }

    public CategoryDto fromEntityToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        return categoryDto;
    }

    public List<CategoryDto> fromPageToList(Page<Category> categories) {
        return categories.getContent().stream()
                .map(this::fromEntityToCategoryDto)
                .collect(Collectors.toList());
    }
}
