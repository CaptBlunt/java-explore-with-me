package ru.practicum.ewm.maper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilation;
import ru.practicum.ewm.dto.compilation.UpdateCompilation;
import ru.practicum.ewm.entity.Compilation;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation fromNewCompilationToEntity(NewCompilation newCompilation) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilation.getTitle());

        return compilation;
    }

    public CompilationDto fromEntityToCompilationDto(Compilation compilation) {
        CompilationDto compilationDto = new CompilationDto();

        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle() == null ? null : compilation.getTitle());
        compilationDto.setEvents(eventMapper.fromListEventToListEventShortDto(compilation.getEvent()));

        return compilationDto;
    }

    public List<CompilationDto> fromPageCompilationToListCompilationDto(Page<Compilation> compilations) {
        return compilations.getContent().stream()
                .map(this::fromEntityToCompilationDto)
                .collect(Collectors.toList());
    }

    public Compilation fromUpdateCompilationToEntity(UpdateCompilation updateCompilation, Compilation compilationUpd) {
        Compilation compilation = new Compilation();

        compilation.setPinned(updateCompilation.getPinned() == null ? compilationUpd.getPinned() : updateCompilation.getPinned());
        compilation.setTitle(updateCompilation.getTitle() == null ? null : updateCompilation.getTitle());
        return compilation;
    }
}
