package tm.ugur.dto;


import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlaceSubCategoryDTO extends AbstractDTO{


    private Map<String, String> titles;

    public PlaceSubCategoryDTO() {
    }

    public PlaceSubCategoryDTO(Map<String, String> titles) {
        this.titles = titles;
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
        PlaceSubCategoryDTO that = (PlaceSubCategoryDTO) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PlaceSubCategoryDTO{" +
                "titles=" + titles +
                ", id=" + id +
                '}';
    }
}
