package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;

import java.util.List;

@Repository
public interface StartRouteStopRepository extends JpaRepository<StartRouteStop, Long> {
    List<StartRouteStop> findByStop(Stop stop);
    List<StartRouteStop> findByRouteOrderByIndex(Route route);
    List<StartRouteStop> findByStopAndRoute(Stop stop, Route route);
    Boolean existsByRoute(Route route);

}
