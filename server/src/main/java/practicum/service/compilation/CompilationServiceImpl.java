package practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practicum.dto.compilation.CompilationDto;
import practicum.dto.compilation.CompilationDtoIn;
import practicum.dto.compilation.CompilationDtoUpdate;
import practicum.dto.mappers.compilation.CompilationMapper;
import practicum.exceptions.NotFoundException;
import practicum.model.Compilation;
import practicum.model.Event;
import practicum.repository.CompilationRepository;
import practicum.repository.EventRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return compilationRepository.findAllByPinned(pinned, pageable).stream()
                .map(compilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long comId) {
        Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена или недоступна"));
        return compilationMapper.toDto(compilation);
    }

    @Override
    public CompilationDto createCompilation(CompilationDtoIn compilationDtoIn) {
        Compilation compilation = new Compilation(compilationDtoIn.getPinned(), compilationDtoIn.getTitle());
        Set<Long> eventsIds = Collections.EMPTY_SET;
        if (compilationDtoIn.getEvents() != null) {
            eventsIds = compilationDtoIn.getEvents();
        }
        List<Event> events = eventRepository.findAllById(eventsIds);
        compilation.setEvents(new HashSet<>(events));
        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long comId) {
        if (compilationRepository.existsById(comId)) {
            compilationRepository.deleteById(comId);
        } else {
            throw new NotFoundException("Подборка не найдена или недоступна");
        }
    }

    @Override
    public CompilationDto changeCompilation(Long comId, CompilationDtoUpdate compilationDtoUpdate) {
        Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена или недоступна"));
        if (compilationDtoUpdate.getPinned() != null) {
            compilation.setPinned(compilationDtoUpdate.getPinned());
        }
        if (compilationDtoUpdate.getTitle() != null) {
            compilation.setTitle(compilationDtoUpdate.getTitle());
        }
        if (compilationDtoUpdate.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationDtoUpdate.getEvents());
            compilation.setEvents(new HashSet<>(events));
        }
        compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }
}
