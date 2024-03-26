package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "place_categories")
public class PlaceCategory extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "placeCategory")
    private List<PlaceCategoryTranslation> translations;

    @OneToMany(mappedBy = "placeCategory")
    private List<PlaceSubCategory> subCategories;

    @OneToMany(mappedBy = "placeCategory")
    private List<Place> places;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public PlaceCategory(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PlaceSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<PlaceSubCategory> subCategories) {
        this.subCategories = subCategories;
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


    public List<PlaceCategoryTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<PlaceCategoryTranslation> translations) {
        this.translations = translations;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategory that = (PlaceCategory) object;
        return id == that.id && Objects.equals(translations, that.translations)
                && Objects.equals(subCategories, that.subCategories)
                && Objects.equals(places, that.places)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, translations, subCategories, places, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "PlaceCategory{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
