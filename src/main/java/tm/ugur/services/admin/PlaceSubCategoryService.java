package tm.ugur.services.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.errors.placeSubCategories.PlaceSubCategoryNotFoundException;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.request.place.SubCategoryRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceSubCategoryService {

    private final PlaceSubCategoryRepository placeSubCategoryRepository;
    private final PlaceSubCategoryTranslationService translationService;
    private final PlaceCategoryService placeCategoryService;

    public PlaceSubCategoryService(PlaceSubCategoryRepository placeSubCategoryRepository,
                                   PlaceSubCategoryTranslationService translationService,
                                   PlaceCategoryService placeCategoryService) {
        this.placeSubCategoryRepository = placeSubCategoryRepository;
        this.translationService = translationService;
        this.placeCategoryService = placeCategoryService;
    }

    public List<PlaceSubCategory> findAll(){
        return placeSubCategoryRepository.findAll();
    }


    @Transactional
    public void store(SubCategoryRequest request){
        PlaceSubCategory placeSubCategory = new PlaceSubCategory();
        placeSubCategory.setPlaceCategory(placeCategoryService.findOne(request.getPlaceCategory()));
        placeSubCategory.setUpdatedAt(new Date());
        placeSubCategory.setCreatedAt(new Date());
        List<PlaceSubCategoryTranslation> psct =
                new ArrayList<>(List.of(translationService.store(new PlaceSubCategoryTranslation("tm", request.getTitleTm())),
                        translationService.store(new PlaceSubCategoryTranslation("ru", request.getTitleRu())),
                        translationService.store(new PlaceSubCategoryTranslation("en", request.getTitleEn()))));
        placeSubCategory.setTranslations(psct);
        PlaceSubCategory newPlaceSubCategory = placeSubCategoryRepository.save(placeSubCategory);
        psct.forEach(p -> p.setPlaceSubCategory(newPlaceSubCategory));

    }

    @Transactional
    public PlaceSubCategory store(PlaceSubCategory placeSubCategory){
        placeSubCategory.setCreatedAt(new Date());
        placeSubCategory.setUpdatedAt(new Date());
        return placeSubCategoryRepository.save(placeSubCategory);
    }

    public PlaceSubCategory findOne(long id){
        return placeSubCategoryRepository.findById(id).orElseThrow(PlaceSubCategoryNotFoundException::new);
    }


    @Transactional
    public void update(long id, SubCategoryRequest request){
        PlaceSubCategory subCategory = findOne(id);
        subCategory.setPlaceCategory(placeCategoryService.findOne(request.getPlaceCategory()));
            PlaceSubCategoryTranslation existingTranslationTm = getTranslation(subCategory.getTranslations(),"tm");
            PlaceSubCategoryTranslation existingTranslationRu = getTranslation(subCategory.getTranslations(),"ru");
            PlaceSubCategoryTranslation existingTranslationEn = getTranslation(subCategory.getTranslations(),"en");

            if (existingTranslationTm != null)
                translationService.update(existingTranslationTm, request.getTitleTm());

            if (existingTranslationRu != null)
                translationService.update(existingTranslationRu, request.getTitleRu());

            if (existingTranslationEn != null)
                translationService.update(existingTranslationEn, request.getTitleEn());

            subCategory.setUpdatedAt(new Date());
            placeSubCategoryRepository.save(subCategory);

    }

    private PlaceSubCategoryTranslation getTranslation(List<PlaceSubCategoryTranslation> translations, String locale){
        return translations.stream()
                .filter(translation -> translation.getLocale().equals(locale))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void delete(Long id){
        placeSubCategoryRepository.deleteById(id);
    }
}
