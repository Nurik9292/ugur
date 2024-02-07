package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Client;
import tm.ugur.models.Route;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findClientByPhone(String phone);

    Optional<Client> findClientByRoutesAndId(Route route, Long id);
}
