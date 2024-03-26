package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceSubCategoryTranslation;

@Repository
public interface PlaceSubCategoryTranslationRepository extends JpaRepository<PlaceSubCategoryTranslation, Long> {
}
