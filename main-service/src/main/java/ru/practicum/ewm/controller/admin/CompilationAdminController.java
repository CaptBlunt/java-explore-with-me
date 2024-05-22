package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilation;
import ru.practicum.ewm.dto.compilation.UpdateCompilation;
import ru.practicum.ewm.service.implimitation.CompilationServiceImpl;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class CompilationAdminController {

    private final CompilationServiceImpl compilationService;

    @PostMapping(path = "/compilations")
    @ResponseStatus(code = HttpStatus.CREATED)
    private CompilationDto postCompilation(@RequestBody @Valid NewCompilation newCompilation) {
        log.info("Пришёл запрос /admin/compilations {}", newCompilation);
        CompilationDto response = compilationService.createCompilation(newCompilation);
        log.info("Отправлен ответ postCompilation {}", response);
        return response;
    }

    @DeleteMapping(path = "/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteCompilation(@PathVariable Integer compId) {
        log.info("Пришёл запрос /admin/compilations/{}", compId);
        compilationService.deleteCompilation(compId);
        log.info("Подборка с id = " + compId + " удалена");
    }

    @PatchMapping(path = "/compilations/{compId}")
    private CompilationDto patchCompilation(@RequestBody @Valid UpdateCompilation compilation, @PathVariable Integer compId) {
        log.info("Пришёл запрос /admin/compilations/{}", compId);
        CompilationDto response = compilationService.updateCompilation(compilation, compId);
        log.info("Отправлен ответ patchCompilation {}", response);
        return response;
    }
}
