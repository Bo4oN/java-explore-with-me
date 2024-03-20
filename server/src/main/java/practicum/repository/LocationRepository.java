package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
