package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.model.StatsMapper;
import ru.practicum.repository.StatsRepository;
import statsDto.StatsDto;
import statsDto.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveStats(StatsDto statsDto) {
        repository.save(StatsMapper.toStats(statsDto));
    }

    @Override
    public List<StatsDtoOut> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean isUnique) {
        List<StatsDtoOut> statsList = Collections.emptyList();
        if (uris == null) {
            if (isUnique) {
                statsList = repository.getAllStatisticsUnique(start, end);
            } else {
                statsList = repository.getAllStatistics(start, end);
                StatsDtoOut sto = statsList.get(0);
                log.info("!!!!!!!!! - {}", statsList.size());
                log.info("!!!!!!!!!!!!!!!! - {}", repository.findAll());
                log.info("!!!!!!!DATA!!!!!!! {} - {} - {}", sto.getApp(), sto.getUri(), sto.getHits());
            }
        } else if (uris.size() == 1) {
            if (isUnique) {
                statsList = repository.getAllStatisticsByUriUnique(start, end, uris.get(0));
            } else {
                statsList = repository.getAllStatisticsByUri(start, end, uris.get(0));
            }
        } else if (uris.size() == 2) {
            if (isUnique) {
                statsList = repository.getAllStatisticsByUriUnique(start, end, uris.get(0), uris.get(1));
            } else {
                statsList = repository.getAllStatisticsByUri(start, end, uris.get(0), uris.get(1));
            }
        }
        return statsList;
    }
}
