package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Route;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query(value = "SELECT new tm.ugur.models.Route(r.id, r.name, r.interval, r.number, r.city) FROM Route r")
    List<Route> findAllWithIdNameIntervalNumberCityRouteTime();

    Optional<Route> findRouteByClientsAndId(Client client, Long id);

    Optional<Route> findRouteByName(String name);
}
