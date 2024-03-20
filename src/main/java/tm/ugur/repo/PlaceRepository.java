package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByPlaceSubCategory(PlaceSubCategory placeSubCategory);

    List<Place> findByPlaceCategory(PlaceCategory placeCategory);
}
