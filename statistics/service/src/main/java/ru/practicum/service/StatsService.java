package ru.practicum.service;

import stats.StatsDto;
import stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveStats(StatsDto statsDto);

    List<StatsDtoOut> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique);
}
