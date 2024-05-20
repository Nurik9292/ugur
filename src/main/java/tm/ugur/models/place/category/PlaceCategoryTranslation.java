package tm.ugur.models.place.category;

import jakarta.persistence.*;
import tm.ugur.models.AbstractEntity;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "place_category_translations")
public class PlaceCategoryTranslation extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "place_category_id", referencedColumnName = "id")
    private PlaceCategory placeCategory;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public PlaceCategoryTranslation(){}

    public PlaceCategoryTranslation(String title){
        this.title = title;
    }

    public PlaceCategoryTranslation(String locale, String title){
        this.locale = locale;
        this.title = title;
    }

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

    public PlaceCategory getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(PlaceCategory placeCategory) {
        this.placeCategory = placeCategory;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategoryTranslation that = (PlaceCategoryTranslation) object;
        return id == that.id && Objects.equals(locale, that.locale) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locale, title);
    }

    @Override
    public String toString() {
        return "PlaceCategoryTranslation{" +
                "id=" + id +
                ", locale='" + locale + '\'' +
                ", title='" + title + '\'' +
                '}';
    }


}
