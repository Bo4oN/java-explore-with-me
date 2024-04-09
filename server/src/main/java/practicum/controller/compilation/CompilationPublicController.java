package practicum.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practicum.dto.compilation.CompilationDto;
import practicum.service.compilation.CompilationServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/compilations")
@Validated
public class CompilationPublicController {
    private final CompilationServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) boolean pinned,
                                                   @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
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
