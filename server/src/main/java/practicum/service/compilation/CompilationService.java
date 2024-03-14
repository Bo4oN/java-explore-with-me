package practicum.service.compilation;

import practicum.dto.compilation.CompilationDto;
import practicum.dto.compilation.CompilationDtoIn;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAllCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long comId);

    CompilationDto createCompilation(CompilationDtoIn compilationDtoIn);

    void deleteCompilation(Long comId);

    CompilationDto changeCompilation(Long comId, CompilationDtoIn compilationDtoIn);
}
