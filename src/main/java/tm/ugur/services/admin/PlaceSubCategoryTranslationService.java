package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;
import tm.ugur.repo.PlaceSubCategoryTranslationRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceSubCategoryTranslationService {

    private final PlaceSubCategoryTranslationRepository translationRepository;

    @Autowired
    public PlaceSubCategoryTranslationService(PlaceSubCategoryTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Transactional
    public PlaceSubCategoryTranslation store(PlaceSubCategoryTranslation placeSubCategoryTranslation){
        placeSubCategoryTranslation.setCreatedAt(new Date());
        placeSubCategoryTranslation.setUpdatedAt(new Date());
        return translationRepository.save(placeSubCategoryTranslation);
    }

    @Transactional
    public void update(PlaceSubCategoryTranslation placeSubCategoryTranslation, String title){
        placeSubCategoryTranslation.setTitle(title);
        placeSubCategoryTranslation.setUpdatedAt(new Date());
        translationRepository.save(placeSubCategoryTranslation);
    }

    public List<PlaceSubCategoryTranslation> findByLocaleAndTitle(String locale, String title){
        return translationRepository.findByLocaleAndTitle(locale, title);
    }
}
