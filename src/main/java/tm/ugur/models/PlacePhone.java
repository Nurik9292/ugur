package tm.ugur.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "place_phones")
public class PlacePhone extends AbstractEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "place_phone_id", referencedColumnName = "id")
    private Place place;


    public PlacePhone(){

    }

    public PlacePhone(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlacePhone phone = (PlacePhone) object;
        return id == phone.id && Objects.equals(number, phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
