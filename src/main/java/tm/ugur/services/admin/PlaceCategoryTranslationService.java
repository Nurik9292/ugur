package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.place.category.PlaceCategoryTranslation;
import tm.ugur.repo.PlaceCategoryTranslationRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceCategoryTranslationService {

    private final PlaceCategoryTranslationRepository translationRepository;

    @Autowired
    public PlaceCategoryTranslationService(PlaceCategoryTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Transactional
    public PlaceCategoryTranslation store(PlaceCategoryTranslation placeCategoryTranslation){
        placeCategoryTranslation.setCreatedAt(new Date());
        placeCategoryTranslation.setUpdatedAt(new Date());
        return translationRepository.save(placeCategoryTranslation);
    }

    @Transactional
    public void update(PlaceCategoryTranslation placeCategoryTranslation, String title){
        placeCategoryTranslation.setTitle(title);
        placeCategoryTranslation.setUpdatedAt(new Date());
        translationRepository.save(placeCategoryTranslation);
    }

    public Optional<PlaceCategoryTranslation> findByLocaleAndTitle(String locale, String title){
        return translationRepository.findByLocaleAndTitle(locale, title);
    }
}
