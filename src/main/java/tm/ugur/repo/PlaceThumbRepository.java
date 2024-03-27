package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceThumb;

@Repository
public interface PlaceThumbRepository extends JpaRepository<PlaceThumb, Long> {
}
