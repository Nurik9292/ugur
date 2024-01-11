package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

@Entity
@Table(name = "stops")
public class Stop {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private int id;

    @NotEmpty(message = "Заполните название.")
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private StringBuilder location;

    @Transient
    private StringBuilder lat;

    @Transient
    private StringBuilder lng;

    public Stop(){

    }

    public Stop(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringBuilder getLocation() {
        return location;
    }

    public void setLocation(StringBuilder location) {
        this.location = location;
    }

    public StringBuilder getLat() {
        return lat;
    }

    public void setLat(StringBuilder lat) {
        this.lat = lat;
    }

    public StringBuilder getLng() {
        return lng;
    }

    public void setLng(StringBuilder lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return id == stop.id && Objects.equals(name, stop.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Stop{" +
             "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
