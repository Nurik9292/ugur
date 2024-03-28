package tm.ugur.seed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceSubCategoryTranslationService;
import tm.ugur.services.parser.ParserService;
import tm.ugur.services.parser.PlaceUgur;

import java.util.List;

@Component
@Order(13)
public class ParserPlaceSeed implements CommandLineRunner {

    private final PlaceService placeService;
    private final ParserService parserService;
    private final PlaceSubCategoryTranslationService translationService;

    @Autowired
    public ParserPlaceSeed(PlaceService placeService,
                           ParserService parserService,
                           PlaceSubCategoryTranslationService translationService) {
        this.placeService = placeService;
        this.parserService = parserService;
        this.translationService = translationService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(placeService.findAll().isEmpty()){
            List<PlaceUgur> places = parserService.parser();

            places.forEach(place -> {
                place.getCategories().forEach(category -> {
                    List<PlaceSubCategoryTranslation> translation = translationService.findByLocaleAndTitle("ru", category.getName_ru());
                    System.out.println(translation);

                    if(!translation.isEmpty()){
                        PlaceSubCategory sub = translation.getFirst().getPlaceSubCategory();
                        PlaceCategory placeCategory = sub.getPlaceCategory();
                        placeService.store(placeCategory,
                                sub,
                                place.getName_tm(),
                                place.getName_ru(),
                                place.getAddress_tm(),
                                place.getAddress_ru(),
                                place.getImage(),
                                place.getLatitude(),
                                place.getLongitude());

                    }
                });
            });
        }

    }
}
