package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.place.Place;
import tm.ugur.models.place.PlacePhone;

@Repository
public interface PlacePhoneRepository extends JpaRepository<PlacePhone, Long> {
    Boolean existsByNumberAndPlace(String number, Place place);
}
