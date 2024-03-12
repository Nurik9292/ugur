package tm.ugur.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import tm.ugur.models.PlaceCategory;

import java.util.Objects;

public class PlaceSubCategoryDTO extends AbstractDTO{

    private String title;


    public PlaceSubCategoryDTO() {
    }

    public PlaceSubCategoryDTO(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceSubCategoryDTO that = (PlaceSubCategoryDTO) object;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "PlaceSubCategoryDTO{" +
                "title='" + title + '\'' +
                ", id=" + id +
                '}';
    }
}
