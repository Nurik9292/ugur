package tm.ugur.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.util.errors.placeSubCategories.PlaceSubCategoryNotFoundException;
import tm.ugur.util.pagination.PaginationUtil;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PlaceSubCategoryService {

    private final PlaceSubCategoryRepository placeSubCategoryRepository;
    private final PaginationUtil paginationUtil;
    private final PlaceSubCategoryTranslationService translationService;

    public PlaceSubCategoryService(PlaceSubCategoryRepository placeSubCategoryRepository,
                                   PaginationUtil paginationUtil,
                                   PlaceSubCategoryTranslationService translationService) {
        this.placeSubCategoryRepository = placeSubCategoryRepository;
        this.paginationUtil = paginationUtil;
        this.translationService = translationService;
    }

    public List<PlaceSubCategory> findAll(){
        return placeSubCategoryRepository.findAll();
    }

    public Page<PlaceSubCategory> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationUtil.createPage(placeSubCategoryRepository.findAll(), pageNumber, itemsPerPage);
    }


    public Page<PlaceSubCategory> getPlaceSubCategoryPages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<PlaceSubCategory> placeCategories = !sortBy.isBlank()
                ? subCategorySorted(sortBy) : placeSubCategoryRepository.findAll();;

        return this.paginationUtil.createPage(placeCategories, pageNumber, itemsPerPage);
    }

    private List<PlaceSubCategory> subCategorySorted(String sortBy){
        return placeSubCategoryRepository.findAll().stream()
                .sorted(Comparator.comparing(sub -> {
                    PlaceSubCategoryTranslation subTranslation = sub.getTranslations().stream()
                            .filter(trans -> trans.getLocale().equals(sortBy))
                            .findFirst().orElse(null);

                    return subTranslation != null ? subTranslation.getTitle() : "";
                })).toList();
    }

    @Transactional
    public void store(PlaceSubCategory placeSubCategory, String title_tm, String title_ru, String title_en){
        placeSubCategory.setUpdatedAt(new Date());
        placeSubCategory.setCreatedAt(new Date());
        List<PlaceSubCategoryTranslation> psct =
                new ArrayList<>(List.of(translationService.store(new PlaceSubCategoryTranslation("tm", title_tm)),
                        translationService.store(new PlaceSubCategoryTranslation("ru", title_ru)),
                        translationService.store(new PlaceSubCategoryTranslation("en", title_en))));
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
    public void update(long id, PlaceSubCategory placeSubCategory, String title_tm, String title_ru, String title_en){
        PlaceSubCategory subCategory = findOne(id);

            PlaceSubCategoryTranslation existingTranslationTm = getTranslation(subCategory.getTranslations(),"tm");
            PlaceSubCategoryTranslation existingTranslationRu = getTranslation(subCategory.getTranslations(),"ru");
            PlaceSubCategoryTranslation existingTranslationEn = getTranslation(subCategory.getTranslations(),"en");

            if (existingTranslationTm != null)
                translationService.update(existingTranslationTm, title_tm);

            if (existingTranslationTm != null)
                translationService.update(existingTranslationRu, title_ru);

            if (existingTranslationTm != null)
                translationService.update(existingTranslationEn, title_en);

            placeSubCategory.setId(id);
            placeSubCategory.setUpdatedAt(new Date());
            placeSubCategory.setCreatedAt(subCategory.getCreatedAt());
            placeSubCategoryRepository.save(placeSubCategory);

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
