package tm.ugur.util.sort.place;

import org.springframework.stereotype.Component;
import tm.ugur.models.place.Place;
import tm.ugur.models.place.PlaceTranslation;
import tm.ugur.util.sort.Sort;

import java.util.*;

@Component
public class SortPlace implements Sort {

    private List<Place> places;
    private String sortBy;

    @Override
    public void execute() {

        if(Objects.nonNull(sortBy))
            sort();
    }

    private void sort() {
        if (sortBy.equals("title"))
            sortPlaceByTitle();
        else
            sortPlaceByAddress();

    }

    private void sortPlaceByTitle() {
       Map<Long, PlaceTranslation> translations = processPlace();
        places.sort(Comparator.comparing(place -> translations.get(place.getId()).getTitle()));
    }

    private void sortPlaceByAddress() {
        Map<Long, PlaceTranslation> translations = processPlace();
        places.sort(Comparator.comparing(place -> translations.get(place.getId()).getAddress()));
    }

    private Map<Long, PlaceTranslation> processPlace() {
        Map<Long, PlaceTranslation> translations = new HashMap<>();
        places.forEach(place -> {
            place.getTranslations().forEach(translation ->{
                if(translation.getLocale().equals("ru"))
                    translations.put(place.getId(), translation);
            });
        });
        return translations;
    }



    public void setData(List<Place> places, String sortBy) {
        this.places = places;
        this.sortBy = sortBy;
    }

}
