package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stats;
import stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where st.creationTime between ?1 and ?2 " +
            "group by st.app, st.uri")
    List<StatsDtoOut> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where st.creationTime between ?1 and ?2 " +
            "group by st.app, st.uri, st.ip")
    List<StatsDtoOut> getAllStatisticsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where (st.creationTime between ?1 and ?2) " +
            "and st.uri = ?3 " +
            "group by st.app, st.uri")
    List<StatsDtoOut> getAllStatisticsByUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where (st.creationTime between ?1 and ?2) " +
            "and st.uri = ?3 " +
            "group by st.app, st.uri, st.ip")
    List<StatsDtoOut> getAllStatisticsByUriUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where (st.creationTime between ?1 and ?2) " +
            "and (st.uri = ?3 or st.uri = ?4) " +
            "group by st.app, st.uri")
    List<StatsDtoOut> getAllStatisticsByUri(LocalDateTime start, LocalDateTime end,
                                            String firstUri, String secondUri);

    @Query("select st.app as app, st.uri as uri, COUNT(st.app) as hits from Stats as st " +
            "where (st.creationTime between ?1 and ?2) " +
            "and (st.uri = ?3 or st.uri = ?4) " +
            "group by st.app, st.uri, st.ip")
    List<StatsDtoOut> getAllStatisticsByUriUnique(LocalDateTime start, LocalDateTime end,
                                                  String firstUri, String secondUri);
}
