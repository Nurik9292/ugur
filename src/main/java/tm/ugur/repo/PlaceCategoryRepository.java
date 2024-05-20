package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.place.category.PlaceCategory;

@Repository
public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Long> {

}
