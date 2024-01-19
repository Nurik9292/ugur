package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;

import java.util.List;

@Repository
public interface EndRouteStopRepository extends JpaRepository<EndRouteStop, Long> {
    List<EndRouteStop> findByStop(Stop stop);

    List<EndRouteStop> findByStopAndRoute(Stop stop, Route route);
}
