package tm.ugur.util.sort.place;

import org.springframework.stereotype.Component;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.category.PlaceCategoryTranslation;
import tm.ugur.util.sort.Sort;

import java.util.Comparator;
import java.util.List;

@Component
public class SortCategory implements Sort {

    private List<PlaceCategory> categories;
    private String sortBy;

    @Override
    public void execute() {
        categories.sort(Comparator.comparing(category -> category.getTranslations().stream()
                .filter(translation -> translation.getLocale().equals(sortBy))
                .map(PlaceCategoryTranslation::getTitle).findFirst().orElse("")));
    }


    public void setData(List<PlaceCategory> categories, String sortBy) {
        this.categories = categories;
        this.sortBy = sortBy;
    }
}
