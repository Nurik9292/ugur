package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceCategoryTranslation;

@Repository
public interface PlaceCategoryTranslationRepository extends JpaRepository<PlaceCategoryTranslation, Long> {
}
