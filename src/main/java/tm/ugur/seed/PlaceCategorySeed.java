package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceCategoryTranslation;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceCategoryTranslationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Order(10)
public class PlaceCategorySeed implements CommandLineRunner {

    private final PlaceCategoryService placeCategoryService;
    private final PlaceCategoryTranslationService translationService;

    @Autowired
    public PlaceCategorySeed(PlaceCategoryService placeCategoryService,
                             PlaceCategoryTranslationService translationService) {
        this.placeCategoryService = placeCategoryService;
        this.translationService = translationService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(placeCategoryService.findAll().isEmpty()){
            Map<String, List<PlaceCategoryTranslation>> categories = new HashMap<>(
                    Map.of("cafe", List.of(new PlaceCategoryTranslation("tm", "Kafe"),
                                    new PlaceCategoryTranslation("ru", "Кафе"),
                                    new PlaceCategoryTranslation("en", "Cafe")),
                            "journey", List.of(new PlaceCategoryTranslation("tm", "Syýahat"),
                                    new PlaceCategoryTranslation("ru", "Путешествие"),
                                    new PlaceCategoryTranslation("en", "Journey")),
                            "health", List.of(new PlaceCategoryTranslation("tm", "Saglyk"),
                                    new PlaceCategoryTranslation("ru", "Здоровье"),
                                    new PlaceCategoryTranslation("en", "Health")),
                            "services", List.of(new PlaceCategoryTranslation("tm", "Hyzmatlary"),
                                    new PlaceCategoryTranslation("ru", "Услуги"),
                                    new PlaceCategoryTranslation("en", "Services")),
                            "events", List.of(new PlaceCategoryTranslation("tm", "Wakalar"),
                                    new PlaceCategoryTranslation("ru", "Мероприятия"),
                                    new PlaceCategoryTranslation("en", "Events")),
                            "education", List.of(new PlaceCategoryTranslation("tm", "Bilim"),
                                    new PlaceCategoryTranslation("ru", "Образование"),
                                    new PlaceCategoryTranslation("en", "Education")),
                            "sports and recreation", List.of(new PlaceCategoryTranslation("tm", "Sport we dynç alyş"),
                                    new PlaceCategoryTranslation("ru", "Спорт и Отдых"),
                                    new PlaceCategoryTranslation("en", "Sports and Recreation")),
                            "outlets", List.of(new PlaceCategoryTranslation("tm", "Söwda nokatlary"),
                                    new PlaceCategoryTranslation("ru", "Торговые точки"),
                                    new PlaceCategoryTranslation("en", "Outlets"))
                            ));

            categories.keySet().forEach(category -> {
                List<PlaceCategoryTranslation> translations = new ArrayList<>(categories.get(category));
                PlaceCategory placeCategory = placeCategoryService.store(new PlaceCategory(translations, getImagePath(category)));
                translations.forEach(translation -> {
                    translation.setPlaceCategory(placeCategory);
                    translationService.store(translation);
                });
            });
        }
    }

    private String getImagePath(String category){
        return switch (category){
            default -> "";
            case "cafe" -> "/api/images/place/category/cafe.svg";
            case "journey" -> "/api/images/place/category/shopping-bag.svg";
            case "health" -> "/api/images/place/category/hospital.svh";
            case "services" -> "/api/images/place/category/service.png";
            case "events" -> "/api/images/place/category/event.svg";
            case "education" -> "/api/images/place/category/education.svg";
            case "sports and recreation" -> "/api/images/place/category/sport.svg";
            case "outlets" -> "/api/images/place/category/shop.svg";
        };
    }
}
