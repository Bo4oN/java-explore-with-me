package ru.practicum.model;

import stats.StatsDto;

public class StatsMapper {
    public static Stats toStats(StatsDto statsDto) {
        return Stats.builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .ip(statsDto.getIp())
                .creationTime(statsDto.getTimestamp())
                .build();
    }
}
