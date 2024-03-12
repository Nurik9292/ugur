package tm.ugur.dto;

import jakarta.persistence.*;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceSubCategory;

import java.util.List;
import java.util.Objects;

public class PlaceCategoryDTO extends AbstractDTO{

    private String title;

    private List<PlaceSubCategoryDTO> subCategories;


    public PlaceCategoryDTO() {
    }

    public PlaceCategoryDTO(String title) {
        this.title = title;
    }

    public PlaceCategoryDTO(String title, List<PlaceSubCategoryDTO> subCategories) {
        this.title = title;
        this.subCategories = subCategories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PlaceSubCategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<PlaceSubCategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategoryDTO that = (PlaceCategoryDTO) object;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "PlaceCategoryDTO{" +
                "title='" + title + '\'' +
                ", subCategories=" + subCategories +
                ", id=" + id +
                '}';
    }
}
