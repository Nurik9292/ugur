package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceSubCategoryTranslationService;
import tm.ugur.services.parser.PlaceUgur;
import tm.ugur.services.parser.ParserService;

import java.awt.*;
import java.util.*;
import java.util.List;

@Controller
public class TestController {

    private final ParserService parserService;
    private final PlaceService placeService;
    private final PlaceSubCategoryTranslationService translationService;

    private static final WebClient webClient = WebClient.create();

    @Autowired
    public TestController(ParserService parserService,
                          PlaceService placeService,
                          PlaceSubCategoryTranslationService translationService) {
        this.parserService = parserService;
        this.placeService = placeService;
        this.translationService = translationService;
    }

    @GetMapping("/test")
    public String test(){

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

        return "null";
    }
}
