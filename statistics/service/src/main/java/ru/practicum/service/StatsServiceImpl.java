package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.IncorrectDataException;
import ru.practicum.model.StatsMapper;
import ru.practicum.repository.StatsRepository;
import stats.StatsDto;
import stats.StatsDtoOut;

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
        if (start.isAfter(end)) {
            throw new IncorrectDataException("Некорректные даты");
        }
        List<StatsDtoOut> statsList = Collections.emptyList();
        if (uris == null) {
            if (isUnique) {
                statsList = repository.getAllStatisticsUnique(start, end);
            } else {
                statsList = repository.getAllStatistics(start, end);
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
