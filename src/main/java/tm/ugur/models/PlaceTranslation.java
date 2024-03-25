package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "place_translation")
public class PlaceTranslation extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "locale")
    private String locale;

    @Column(name = "title")
    private String title;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "place_id", referencedColumnName ="id", nullable = false)
    private Place place;

    public PlaceTranslation(){}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        PlaceTranslation that = (PlaceTranslation) object;
        return id == that.id && Objects.equals(locale, that.locale) && Objects.equals(title, that.title) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, locale, title, address);
    }

    @Override
    public String toString() {
        return "PlaceTranslation{" +
                "id=" + id +
                ", locale='" + locale + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", place=" + place +
                '}';
    }
}
