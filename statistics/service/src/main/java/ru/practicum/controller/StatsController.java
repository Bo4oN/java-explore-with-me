package ru.practicum.controller;

import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.StatsServiceImpl;
import stats.StatsDto;
import stats.StatsDtoOut;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {
    private final StatsServiceImpl service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveInformation(@RequestBody StatsDto statsDto) {
        log.info("Save data - {}, to statistics", statsDto);
        service.saveStats(statsDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoOut>> getStatistics(@RequestParam(value = "start")
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                           @NotNull
                                                           LocalDateTime start,
                                                           @RequestParam(value = "end")
                                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                           @NotNull
                                                           LocalDateTime end,
                                                           @RequestParam(required = false, value = "uris") @Nullable List<String> uris,
                                                           @RequestParam(value = "unique", defaultValue = "false") boolean isUnique) {
        log.info("Get statistics from - {} to - {}, uris list - {}, isUnique - {}.", start, end, uris, isUnique);
        List<StatsDtoOut> list = service.getStatistics(start, end, uris, isUnique);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
