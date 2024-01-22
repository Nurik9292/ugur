package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
