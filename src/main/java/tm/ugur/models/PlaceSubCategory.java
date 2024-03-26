package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "place_sub_categories")
public class PlaceSubCategory extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "place_category_id", referencedColumnName = "id")
    private PlaceCategory placeCategory;

    @OneToMany(mappedBy = "placeSubCategory")
    List<Place> places;

    @OneToMany(mappedBy = "placeSubCategory")
    List<PlaceSubCategoryTranslation> translations;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public PlaceSubCategory() {
    }

    public PlaceSubCategory(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlaceCategory getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(PlaceCategory placeCategory) {
        this.placeCategory = placeCategory;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<PlaceSubCategoryTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<PlaceSubCategoryTranslation> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceSubCategory that = (PlaceSubCategory) object;
        return id == that.id && Objects.equals(placeCategory, that.placeCategory)
                && Objects.equals(places, that.places)
                && Objects.equals(translations, that.translations)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placeCategory, places, translations, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "PlaceSubCategory{" +
                "id=" + id +
                ", placeCategory=" + placeCategory +
                ", places=" + places +
                ", translations=" + translations +
                '}';
    }
}
