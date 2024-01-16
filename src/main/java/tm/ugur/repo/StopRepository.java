package tm.ugur.repo;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Stop;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop, Integer> {
    List<Stop> findByNameStartingWith(@NotEmpty(message = "Заполните название.") String name);
}
