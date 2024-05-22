package ru.practicum.ewm.controller.freeforall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.service.implimitation.CategoryServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryServiceImpl categoryService;

    @GetMapping(path = "/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                              @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size, HttpServletRequest request) {
        log.info("Пришёл GET запрос /categories?" + request.getQueryString());
        List<CategoryDto> response = categoryService.getAllCategories(from, size);
        log.info("отправлен ответ getAllCategories {}", response);
        return response;
    }

    @GetMapping(path = "/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("Пришёл GET запрос /categories/" + catId);
        CategoryDto response = categoryService.getCategoryById(catId);
        log.info("Отправлен ответ getCategoryById {}", response);
        return response;
    }
}
