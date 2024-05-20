package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.errors.placeCategories.PlaceCategoryNotFoundException;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.category.PlaceCategoryTranslation;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.request.place.CategoryRequest;
import tm.ugur.storage.FileSystemStorageService;
import tm.ugur.util.pagination.PaginationUtil;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PlaceCategoryService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceCategoryTranslationService translationService;
    private final FileSystemStorageService storageService;

    @Autowired
    public PlaceCategoryService(PlaceCategoryRepository placeCategoryRepository,
                                PlaceCategoryTranslationService translationService,
                                FileSystemStorageService storageService) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.translationService = translationService;
        this.storageService = storageService;
    }


    public List<PlaceCategory> findAll(){
        return placeCategoryRepository.findAll();
    }


    @Transactional
    public void store(CategoryRequest request){
        PlaceCategory placeCategory = new PlaceCategory();
        String pathImage = storageService.store(request.getThumb(), "place/category");
        if (!pathImage.isBlank())
            placeCategory.setImage(pathImage);

        placeCategory.setUpdatedAt(new Date());
        placeCategory.setCreatedAt(new Date());
        List<PlaceCategoryTranslation> pct =
                new ArrayList<>(List.of(translationService.store(new PlaceCategoryTranslation("tm", request.getTitleTm())),
                translationService.store(new PlaceCategoryTranslation("ru", request.getTitleRu())),
                translationService.store(new PlaceCategoryTranslation("en", request.getTitleEn()))));
        placeCategory.setTranslations(pct);
        PlaceCategory newPlaceCategory = placeCategoryRepository.save(placeCategory);
        pct.forEach(p -> p.setPlaceCategory(newPlaceCategory));
    }

    @Transactional
    public PlaceCategory store(PlaceCategory placeCategory){
        placeCategory.setCreatedAt(new Date());
        placeCategory.setUpdatedAt(new Date());
        return placeCategoryRepository.save(placeCategory);
    }

    public PlaceCategory findOne(long id){
        return placeCategoryRepository.findById(id).orElseThrow(PlaceCategoryNotFoundException::new);
    }


    @Transactional
    public void update(long id, CategoryRequest request){
        PlaceCategory existingPlaceCategory = findOne(id);

            String pathImage = storageService.store(request.getThumb(), "place/category");

            if (!pathImage.isBlank()){
                if(Objects.nonNull(existingPlaceCategory.getImage()))
                    storageService.delete(existingPlaceCategory.getImage());
                existingPlaceCategory.setImage(pathImage);
            }

            PlaceCategoryTranslation existingTranslationTm = getTranslation(existingPlaceCategory.getTranslations(),"tm");
            PlaceCategoryTranslation existingTranslationRu = getTranslation(existingPlaceCategory.getTranslations(),"ru");
            PlaceCategoryTranslation existingTranslationEn = getTranslation(existingPlaceCategory.getTranslations(),"en");

            if (existingTranslationTm != null)
                translationService.update(existingTranslationTm, request.getTitleTm());

            if (existingTranslationRu != null)
                translationService.update(existingTranslationRu, request.getTitleRu());

            if (existingTranslationEn != null)
                translationService.update(existingTranslationEn, request.getTitleEn());


            existingPlaceCategory.setUpdatedAt(new Date());
            placeCategoryRepository.save(existingPlaceCategory);


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


    public Map<Long, String> getCategoryTitles(List<PlaceCategory> placeCategories){
        Map<Long, String> titles = new HashMap<>();

        placeCategories.forEach(placeCategory -> {
            placeCategory.getTranslations().stream()
                    .filter(pct -> pct.getLocale().equals("ru"))
                    .findFirst()
                    .ifPresent(pct -> titles.put(placeCategory.getId(), pct.getTitle()));
        });

        return titles;
    }
}
