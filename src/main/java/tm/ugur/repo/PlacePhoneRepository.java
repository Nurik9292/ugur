package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlacePhone;

@Repository
public interface PlacePhoneRepository extends JpaRepository<PlacePhone, Long> {
}
