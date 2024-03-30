package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.PlaceTranslation;

import java.util.Optional;

@Repository
public interface PlaceTranslationRepository extends JpaRepository<PlaceTranslation, Long> {

    Optional<PlaceTranslation> findByLocaleAndTitle(String locale, String title);
}
