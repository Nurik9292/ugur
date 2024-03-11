package tm.ugur.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "place_categories")
public class PlaceCategory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;


    @OneToMany(mappedBy = "route")
    private List<PlaceSubCategory> subCategories;

    public PlaceCategory(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PlaceSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<PlaceSubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceCategory that = (PlaceCategory) object;
        return id == that.id && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "PlaceCategory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
