package ru.practicum.ewm.controller.freeforall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.implimitation.CompilationServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationServiceImpl compilationService;

    @GetMapping(path = "/compilations/{compId}")
    private CompilationDto getCompilationById(@PathVariable Integer compId) {
        log.info("Пришёл GET запрос /compilations/{}", compId);
        CompilationDto response = compilationService.getCompilation(compId);
        log.info("Отправлен ответ getCompilationById {}", response);
        return response;
    }

    @GetMapping(path = "/compilations")
    private List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero(message = "Значение from доложно быть положительным") Integer from,
                                                 @RequestParam(defaultValue = "10") @PositiveOrZero(message = "Значение size доложно быть положительным") Integer size,
                                                 HttpServletRequest request) {
        log.info("Пришёл GET запрос /compilations?" + request.getQueryString());
        List<CompilationDto> response = compilationService.getCompilationsWithParams(pinned, from, size);
        log.info("Отправлен ответ getCompilations {}", response);
        return response;
    }
}
