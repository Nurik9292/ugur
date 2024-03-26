package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceCategoryTranslation;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.util.pagination.PaginationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PaginationService paginationService;
    private final PlaceCategoryTranslationService translationService;

    @Autowired
    public PlaceCategoryService(PlaceCategoryRepository placeCategoryRepository,
                                PaginationService paginationService,
                                PlaceCategoryTranslationService translationService) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.paginationService = paginationService;
        this.translationService = translationService;
    }


    public List<PlaceCategory> findAll(){
        return placeCategoryRepository.findAll();
    }

    public Page<PlaceCategory> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationService.createPage(placeCategoryRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<PlaceCategory> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationService.createPage(placeCategoryRepository.findAll(), pageNumber, itemsPerPage);
    }


    public Page<PlaceCategory> getPlaceCategoryPages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<PlaceCategory> placeCategories = !sortBy.isBlank()
                ? placeCategoryRepository.findAll(Sort.by(sortBy)) : placeCategoryRepository.findAll();;

        return this.paginationService.createPage(placeCategories, pageNumber, itemsPerPage);
    }

    @Transactional
    public void store(PlaceCategory placeCategory, String title_tm, String title_ru, String title_en){
        placeCategory.setUpdatedAt(new Date());
        placeCategory.setCreatedAt(new Date());
        List<PlaceCategoryTranslation> pct =
                new ArrayList<>(List.of(translationService.store(new PlaceCategoryTranslation("tm", title_tm)),
                translationService.store(new PlaceCategoryTranslation("ru", title_ru)),
                translationService.store(new PlaceCategoryTranslation("en", title_en))));
        placeCategory.setTranslations(pct);
        PlaceCategory newPlaceCategory = placeCategoryRepository.save(placeCategory);
        pct.forEach(p -> p.setPlaceCategory(newPlaceCategory));
    }

    public Optional<PlaceCategory> findOne(long id){
        return placeCategoryRepository.findById(id);
    }


    @Transactional
    public void update(long id, PlaceCategory placeCategory, String title_tm, String title_ru, String title_en){
        Optional<PlaceCategory> existingPlaceCategory = findOne(id);

        existingPlaceCategory.ifPresent(category -> {

            PlaceCategoryTranslation existingTranslationTm = getTranslation(category.getTranslations(),"tm");
            PlaceCategoryTranslation existingTranslationRu = getTranslation(category.getTranslations(),"ru");
            PlaceCategoryTranslation existingTranslationEn = getTranslation(category.getTranslations(),"en");

            if (existingTranslationTm != null)
                translationService.update(existingTranslationTm, title_tm);

            if (existingTranslationTm != null)
                translationService.update(existingTranslationRu, title_ru);

            if (existingTranslationTm != null)
                translationService.update(existingTranslationEn, title_en);


            placeCategory.setId(id);
            placeCategory.setUpdatedAt(new Date());
            placeCategory.setCreatedAt(category.getCreatedAt());
            placeCategoryRepository.save(placeCategory);

        });
    }

    private PlaceCategoryTranslation getTranslation(List<PlaceCategoryTranslation> translations, String locale){
        return translations.stream()
                .filter(translation -> translation.getLocale().equals(locale))
                .findFirst()
                .orElse(null);
    }


    @Transactional
    public void delete(Long id){
        this.placeCategoryRepository.deleteById(id);
    }
}
