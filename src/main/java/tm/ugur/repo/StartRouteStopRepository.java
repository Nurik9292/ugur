package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;

import java.util.List;
import java.util.Optional;

@Repository
public interface StartRouteStopRepository extends JpaRepository<StartRouteStop, Long> {
    List<StartRouteStop> findByStop(Stop stop);
    List<StartRouteStop> findByRouteOrderByIndex(Route route);
    Optional<StartRouteStop> findByStopAndRoute(Stop stop, Route route);
    Boolean existsByRoute(Route route);

}
