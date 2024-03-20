package practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event AS e " +
            "WHERE " +
                "((?1 IS null) " +
                "OR (lower(e.annotation) LIKE concat('%', lower(?1), '%')) " +
                "OR (lower(e.description) LIKE concat('%', lower(?1), '%'))) " +
            "AND (e.category.id IN ?2 OR ?2 IS null) " +
            "AND (e.paid = ?3 OR ?3 IS null) " +
            "AND (e.eventDate > ?4 OR ?4 IS null) " +
            "AND (e.eventDate < ?5 OR ?5 IS null) " +
            "AND (?6 = false OR e.participantLimit = 0 OR " +
                "(SELECT COUNT(r.id) FROM Request as r WHERE r.event.id = e.id))"
            )
     List<Event> findAllEventsByFilter(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    //@Query("SELECT e FROM Event AS e " +
    //        "WHERE (e.initiator.id IN ?1 OR ?1 IS null) " +
    //        "AND (e.state IN ?2 OR ?2 IS null) " +
    //        "AND (e.category.id IN ?3 OR ?3 IS null) " +
    //        "AND (e.eventDate > ?4 OR ?4 IS null) " +
    //        "AND (e.eventDate < ?5 OR ?5 IS null)"
    //)
    //List<Event> findAllEventsByAdminFilter(List<Long> usersIds, List<String> states, List<Long> categoriesIds,
    //                                      LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);
}
