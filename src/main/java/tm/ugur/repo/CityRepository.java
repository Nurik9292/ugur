package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
