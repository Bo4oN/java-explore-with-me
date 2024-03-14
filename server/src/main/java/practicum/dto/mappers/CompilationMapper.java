package practicum.dto.mappers;

import practicum.dto.compilation.CompilationDto;
import practicum.dto.compilation.CompilationDtoIn;
import practicum.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(CompilationDtoIn compilationDtoIn) {
        return new Compilation(compilationDtoIn.getPinned(), compilationDtoIn.getTitle());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle(),
                compilation.getEvents().stream()
                        .map(EventMapper::toEventDtoLight)
                        .collect(Collectors.toSet())
        );
    }
}
