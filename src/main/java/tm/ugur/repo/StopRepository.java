package tm.ugur.repo;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Stop;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
    List<Stop> findByNameStartingWith(@NotEmpty(message = "Заполните название.") String name);

    Optional<Stop> findByName(String name);
}
