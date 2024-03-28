package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceCategory;

import java.util.Optional;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {

}
