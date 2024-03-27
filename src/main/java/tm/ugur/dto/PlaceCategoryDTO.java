package tm.ugur.dto;

import jakarta.persistence.*;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceSubCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaceCategoryDTO extends AbstractDTO{

   private Map<String, String> titles;

    private List<PlaceSubCategoryDTO> subCategories;


    public PlaceCategoryDTO() {
    }

    public PlaceCategoryDTO(Map<String, String> titles) {
        this.titles = titles;
    }

    public PlaceCategoryDTO(Map<String, String> titles, List<PlaceSubCategoryDTO> subCategories) {
        this.titles = titles;
        this.subCategories = subCategories;
    }


    public List<PlaceSubCategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<PlaceSubCategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }


    public Map<String, String> getTitles() {
        return titles;
    }

    public void setTitles(Map<String, String> titles) {
        this.titles = titles;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategoryDTO that = (PlaceCategoryDTO) object;
        return Objects.equals(titles, that.titles) && Objects.equals(subCategories, that.subCategories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titles, subCategories);
    }

    @Override
    public String toString() {
        return "PlaceCategoryDTO{" +
                "titles=" + titles +
                ", subCategories=" + subCategories +
                ", id=" + id +
                '}';
    }
}
