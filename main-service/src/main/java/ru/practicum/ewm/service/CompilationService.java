package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilation;
import ru.practicum.ewm.dto.compilation.UpdateCompilation;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilation newCompilation);

    CompilationDto getCompilation(Integer compId);

    CompilationDto updateCompilation(UpdateCompilation updateCompilation, Integer compId);

    List<CompilationDto> getCompilationsWithParams(Boolean pinned, Integer from, Integer size);

    void deleteCompilation(Integer compId);
}
