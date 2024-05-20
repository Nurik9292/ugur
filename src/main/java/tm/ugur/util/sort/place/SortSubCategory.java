package tm.ugur.util.sort.place;

import org.springframework.stereotype.Component;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;
import tm.ugur.util.sort.Sort;

import java.util.Comparator;
import java.util.List;


@Component
public class SortSubCategory implements Sort {

    private List<PlaceSubCategory> subCategories;

    private String sortBy;

    @Override
    public void execute() {
        subCategories.sort(Comparator.comparing(sub -> sub.getTranslations().stream()
                .filter(translation -> translation.getLocale().equals(sortBy))
                .map(PlaceSubCategoryTranslation::getTitle).findFirst().orElse("")));
    }


    public void setData(List<PlaceSubCategory> subCategories, String sortBy) {
        this.subCategories = subCategories;
        this.sortBy = sortBy;
    }
}
