package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "place_thumbs")
public class PlaceThumb extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "path")
    private String path;

    @OneToOne
    @JoinColumn(name = "place_id", referencedColumnName = "id")
    private Place place;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlaceThumb that = (PlaceThumb) object;
        return id == that.id && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path);
    }

    @Override
    public String toString() {
        return "PlaceThumb{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", place=" + place +
                '}';
    }
}
