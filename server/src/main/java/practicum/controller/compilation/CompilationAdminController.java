package practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.dto.compilation.CompilationDto;
import practicum.dto.compilation.CompilationDtoIn;
import practicum.service.compilation.CompilationServiceImpl;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationServiceImpl service;

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid CompilationDtoIn compilationDtoIn) {
        log.info("Запрос на создание подборки событий - {}", compilationDtoIn);
        return service.createCompilation(compilationDtoIn);
    }

    @ResponseBody
    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long comId) {
        log.info("Запрос на удаление подборки событий. ID = {}", comId);
        service.deleteCompilation(comId);
    }

    @ResponseBody
    @PatchMapping("/{comId}")
    public CompilationDto changingCompilation(@PathVariable Long comId,
                                              @RequestBody @Valid CompilationDtoIn compilationDtoIn) {
        log.info("Запрос на изменение подборки событий. ID = {}, новые данные - {}", comId, compilationDtoIn);
        return service.changeCompilation(comId, compilationDtoIn);
    }
}
