package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.Stop;

@Repository
public interface StopRepository extends JpaRepository<Stop, Integer> {

}
