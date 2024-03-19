package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlacePhone;

import java.util.List;

@Repository
public interface PlacePhoneRepository extends JpaRepository<PlacePhone, Long> {

    List<PlacePhone> findByNumber(String number);
}
