package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
