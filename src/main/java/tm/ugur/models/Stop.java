package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stops")
public class Stop {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Id
    private int id;

    @NotEmpty(message = "Заполните название.")
    @Size(min = 3, max = 10, message = "Назвние города должен состоять от 3 до 10 символов.")
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @ManyToOne
    private City city;


    @ManyToMany(mappedBy = "startStops")
    List<Route> startRoutes;

    @ManyToMany(mappedBy = "endStops")
    List<Route> endRoutes;

    @NotEmpty(message = "Заполните поле")
    @Transient
    private String lat;

    @NotEmpty(message = "Заполните поле")

    @Transient
    private String lng;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<Route> getStartRoutes() {
        return startRoutes;
    }

    public void setStartRoutes(List<Route> startRoutes) {
        this.startRoutes = startRoutes;
    }

    public List<Route> getEndRoutes() {
        return endRoutes;
    }

    public void setEndRoutes(List<Route> endRoutes) {
        this.endRoutes = endRoutes;
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
