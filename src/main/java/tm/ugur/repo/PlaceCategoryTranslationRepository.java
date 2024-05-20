package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.place.category.PlaceCategoryTranslation;

import java.util.Optional;

@Repository
public interface PlaceCategoryTranslationRepository extends JpaRepository<PlaceCategoryTranslation, Long> {

    Optional<PlaceCategoryTranslation> findByLocaleAndTitle(String locale, String title);
}
