package ru.practicum.ewm.service.implimitation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategory;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.maper.CategoryMapper;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public PageRequest pagination(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    @Override
    public CategoryDto createCategory(NewCategory newCategory) {
        try {
            Category category = categoryMapper.fromNewCategoryToEntity(newCategory);

            return categoryMapper.fromEntityToCategoryDto(categoryRepository.save(category));

        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Категория с названием " + newCategory.getName() + " уже присутствует в базе";
            throw new DataIntegrityViolationException(errorMessage);
        }
    }

    @Override
    public void deleteCategory(Integer catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id= " + catId + " не найдена"));
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        PageRequest pageable = pagination(from, size);

        return categoryMapper.fromPageToList(categoryRepository.findAll(pageable));
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id= " + catId + " не найдена"));
        return categoryMapper.fromEntityToCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(NewCategory newCategory, Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id= " + catId + " не найдена"));

        category.setName(newCategory.getName());
        categoryRepository.save(category);

        return categoryMapper.fromEntityToCategoryDto(category);
    }
}
