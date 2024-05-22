package ru.practicum.ewm.service.implimitation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilation;
import ru.practicum.ewm.dto.compilation.UpdateCompilation;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.maper.CompilationMapper;
import ru.practicum.ewm.pagination.Pagination;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    private final Pagination pagination;

    public CompilationDto createCompilation(NewCompilation newCompilation) {

        Compilation compilation = compilationMapper.fromNewCompilationToEntity(newCompilation);

        compilation.setPinned(newCompilation.getPinned() != null && newCompilation.getPinned());


        if (newCompilation.getEvents() != null) {
            compilation.setEvent(eventRepository.findAllByIdIn(newCompilation.getEvents()));
        }

        return compilationMapper.fromEntityToCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto getCompilation(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));

        return compilationMapper.fromEntityToCompilationDto(compilation);
    }

    public CompilationDto updateCompilation(UpdateCompilation updateCompilation, Integer compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена"));

        if (updateCompilation.getEvents() != null) {
            compilation.setEvent(eventRepository.findAllByIdIn(updateCompilation.getEvents()));
        }

        compilationRepository.save(compilation);
        return compilationMapper.fromEntityToCompilationDto(compilationRepository.save(compilationMapper.fromUpdateCompilationToEntity(updateCompilation, compilation)));
    }

    public List<CompilationDto> getCompilationsWithParams(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = pagination.pagination(from, size);

        Page<Compilation> compilationPage;

        if (pinned == null) {
            return compilationMapper.fromPageCompilationToListCompilationDto(compilationRepository.findAll(pageRequest));
        }
        compilationPage = compilationRepository.findByPinned(pinned, pageRequest);

        return compilationMapper.fromPageCompilationToListCompilationDto(compilationPage);
    }

    public void deleteCompilation(Integer compId) {
        compilationRepository.deleteById(compId);
    }
}
