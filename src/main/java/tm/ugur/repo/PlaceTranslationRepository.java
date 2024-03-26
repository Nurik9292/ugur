package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceTranslation;

@Repository
public interface PlaceTranslationRepository extends JpaRepository<PlaceTranslation, Long> {
}
