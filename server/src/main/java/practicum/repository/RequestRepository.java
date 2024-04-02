package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.Event;
import practicum.model.Request;
import practicum.model.User;
import practicum.model.enums.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(User user);

    List<Request> findByStatus(String status);

    List<Request> findByEventId(Long eventId);

    int countByEventIdAndStatus(Long eventId, String status);

    boolean existsByEventAndRequester(Event event, User requester);
}
