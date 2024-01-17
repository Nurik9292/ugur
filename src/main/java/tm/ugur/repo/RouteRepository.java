package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
