package tm.ugur.repo;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Client;
import tm.ugur.models.Stop;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
    Optional<Stop> findByName(String name);
    Optional<Stop> findAllByClientsAndId(Client client, Long id);

}
