package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.Request;
import practicum.model.User;
import practicum.model.enums.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(User user);

    List<Request> findByStatus(RequestStatus status);

    List<Request> findByEventId(Long eventId);
}
