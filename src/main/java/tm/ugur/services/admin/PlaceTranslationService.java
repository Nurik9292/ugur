package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.place.PlaceTranslation;
import tm.ugur.repo.PlaceTranslationRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceTranslationService {

    private final PlaceTranslationRepository translationRepository;

    @Autowired
    public PlaceTranslationService(PlaceTranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Transactional
    public PlaceTranslation store(PlaceTranslation placeTranslation){
        placeTranslation.setCreatedAt(new Date());
        placeTranslation.setUpdatedAt(new Date());
        return translationRepository.save(placeTranslation);
    }

    @Transactional
    public PlaceTranslation update(PlaceTranslation placeTranslation){
        placeTranslation.setUpdatedAt(new Date());
        return translationRepository.save(placeTranslation);
    }

    public Optional<PlaceTranslation>  findByTitle(String locale, String title){
        return translationRepository.findByLocaleAndTitle(locale, title);
    }
}
