package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import practicum.model.SubId;
import practicum.model.Subscriber;

import java.util.List;

public interface SubRepository extends JpaRepository<Subscriber, SubId> {

    Boolean existsByUserId(long userId);

    @Query("SELECT s.subId FROM Subscriber as s WHERE s.userId = :userId ")
    List<Long> findAllUserSubscription(long userId);
}
