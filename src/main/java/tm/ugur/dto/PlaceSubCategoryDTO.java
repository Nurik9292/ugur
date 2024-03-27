package tm.ugur.dto;


import java.util.List;
import java.util.Objects;

public class PlaceSubCategoryDTO extends AbstractDTO{

    private List<TranslationDTO> translations;

    public PlaceSubCategoryDTO() {
    }

    public List<TranslationDTO> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationDTO> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceSubCategoryDTO that = (PlaceSubCategoryDTO) object;
        return Objects.equals(translations, that.translations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(translations);
    }

    @Override
    public String toString() {
        return "PlaceSubCategoryDTO{" +
                "translations=" + translations +
                ", id=" + id +
                '}';
    }
}
