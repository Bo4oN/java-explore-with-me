package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Stats;
import stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(st.app) as hits) " +
            "from Stats as st " +
            "where creationTime between :start and :end " +
            "group by st.app, st.uri " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatistics(LocalDateTime start, LocalDateTime end);

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(st.app) as hits) " +
            "from Stats as st " +
            "where creationTime between :start and :end " +
            "group by st.app, st.uri, st.ip " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatisticsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(st.app) as hits) " +
            "from Stats as st " +
            "where (creationTime between :start and :end) " +
            "and st.uri = :uri " +
            "group by st.app, st.uri " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatisticsByUri(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(distinct st.ip) as hits) " +
            "from Stats as st " +
            "where (creationTime between :start and :end) " +
            "and st.uri = :uri " +
            "group by st.app, st.uri, st.ip " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatisticsByUriUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(st.app) as hits) " +
            "from Stats as st " +
            "where (creationTime between :start and :end) " +
            "and (st.uri = :firstUri or st.uri = :secondUri) " +
            "group by st.app, st.uri " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatisticsByUri(LocalDateTime start, LocalDateTime end,
                                            String firstUri, String secondUri);

    @Query("select new stats.StatsDtoOut(st.app as app, st.uri as uri, COUNT(distinct st.ip) as hits) " +
            "from Stats as st " +
            "where (creationTime between :start and :end) " +
            "and (st.uri = :firstUri or st.uri = :secondUri) " +
            "group by st.app, st.uri, st.ip " +
            "order by hits desc")
    List<StatsDtoOut> getAllStatisticsByUriUnique(LocalDateTime start, LocalDateTime end,
                                                  String firstUri, String secondUri);
}
