package tm.ugur.repo;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Client;
import tm.ugur.models.Stop;

import java.util.List;
import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
    Optional<Stop> findByName(String name);
    Optional<Stop> findAllByClientsAndId(Client client, Long id);
    Boolean existsByName(String name);
    @Query(value = "SELECT new tm.ugur.models.Stop(s.id, s.name, s.location, s.city) FROM Stop s")
    List<Stop> findAllWithIdNameLocationCity();

    @Query(value = "SELECT * FROM stops " +
            "ORDER BY location <-> ST_Transform(ST_SetSRID(ST_MakePoint(:latitude, :longitude), 4326), 4326) " +
            "LIMIT 4", nativeQuery = true)
    List<Stop> findNearestStops(@Param("latitude") double latitude, @Param("longitude") double longitude);


}
