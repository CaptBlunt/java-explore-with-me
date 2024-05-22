package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategory;
import ru.practicum.ewm.service.implimitation.CategoryServiceImpl;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryServiceImpl categoryService;

    @PostMapping(path = "/categories")
    @ResponseStatus(code = HttpStatus.CREATED)
    private CategoryDto postCategory(@Valid @RequestBody NewCategory category) {
        log.info("Пришёл POST запрос /categories с телом {}", category);
        CategoryDto response = categoryService.createCategory(category);
        log.info("Отправлен ответ postCategory /categories с телом {}", response);
        return response;
    }

    @PatchMapping(path = "/categories/{catId}")
    private CategoryDto patchCategory(@Valid @RequestBody NewCategory category, @PathVariable Integer catId) {
        log.info("Пришёл запрос /admin/categories/{}", catId);
        CategoryDto response = categoryService.updateCategory(category, catId);
        log.info("Отправлен ответ patchCategory {}", response);
        return response;
    }

    @DeleteMapping(path = "/categories/{catId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    private void deleteCategory(@PathVariable Integer catId) {
        log.info("Пришёл DELETE запрос /categories/{}", catId);
        categoryService.deleteCategory(catId);
        log.info("Категория с id = " + catId + " удалена");
    }
}
