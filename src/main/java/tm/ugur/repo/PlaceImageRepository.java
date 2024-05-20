package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.place.PlaceImage;

@Repository
public interface PlaceImageRepository extends JpaRepository<PlaceImage, Long> {
}
