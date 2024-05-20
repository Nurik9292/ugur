package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;

import java.util.List;

@Repository
public interface PlaceSubCategoryTranslationRepository extends JpaRepository<PlaceSubCategoryTranslation, Long> {

    List<PlaceSubCategoryTranslation> findByLocaleAndTitle(String locale, String title);
}
