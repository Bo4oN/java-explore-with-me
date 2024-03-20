package practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.compilation.CompilationDto;
import practicum.service.compilation.CompilationServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam boolean pinned,
                                                   @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Запрос на получение подборок событий. pinned = {}, from = {}, size = {}", pinned, from, size);
        return service.getAllCompilations(pinned, from, size);
    }

    @ResponseBody
    @GetMapping("/{comId}")
    public CompilationDto getCompilationById(@PathVariable Long comId) {
        log.info("Запрос на получение подборки событий, ID = {}", comId);
        return service.getCompilationById(comId);
    }
}
