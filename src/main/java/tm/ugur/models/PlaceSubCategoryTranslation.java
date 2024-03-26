package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "place_sub_categories")
public class PlaceSubCategoryTranslation extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "place_sub_category_id", referencedColumnName = "id")
    private PlaceSubCategory placeSubCategory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PlaceSubCategory getPlaceSubCategory() {
        return placeSubCategory;
    }

    public void setPlaceSubCategory(PlaceSubCategory placeSubCategory) {
        this.placeSubCategory = placeSubCategory;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceSubCategoryTranslation that = (PlaceSubCategoryTranslation) object;
        return id == that.id && Objects.equals(locale, that.locale) && Objects.equals(title, that.title) && Objects.equals(placeSubCategory, that.placeSubCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locale, title, placeSubCategory);
    }

    @Override
    public String toString() {
        return "PlaceSubCategoryTranslation{" +
                "id=" + id +
                ", locale='" + locale + '\'' +
                ", title='" + title + '\'' +
                ", placeSubCategory=" + placeSubCategory +
                '}';
    }
}
