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

    @Column(name = "image")
    private String image;

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

    public PlaceCategory(List<PlaceCategoryTranslation> translations){
            this.translations = translations;
    }
    public PlaceCategory(List<PlaceCategoryTranslation> translations, String image){
            this.translations = translations;
            this.image = image;
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategory that = (PlaceCategory) object;
        return id == that.id && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image);
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
