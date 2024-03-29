package practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            " WHERE e.state = 'PUBLISHED' " +
            " AND (e.annotation LIKE CONCAT('%',:text,'%') OR e.description LIKE CONCAT('%',:text,'%')) " +
            " AND e.category.id IN :categories " +
            " AND e.paid = :paid " +
            " AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            " AND (" +
            " (:onlyAvailable = true AND e.participantLimit = 0) OR " +
            " (:onlyAvailable = true AND e.participantLimit >" +
            " (SELECT COUNT(r.id) FROM Request as r WHERE r.event.id = e.id AND r.status = 'CONFIRMED')) OR " +
            " (:onlyAvailable = false)) "
    )
     List<Event> findAllEventsByFilter(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, boolean onlyAvailable, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            " WHERE e.initiator.id IN :users " +
            " AND e.state IN :states " +
            " AND e.category.id IN :categories " +
            " AND (e.eventDate BETWEEN :start AND :end) "
    )
    List<Event> findAllEventsByAdminFilter(List<Long> users, List<String> states, List<Long> categories,
                                          LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);
}
