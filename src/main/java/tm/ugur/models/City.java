package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class City implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Заполните название города")
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "city")
    private List<Stop> stops;

    @OneToMany(mappedBy = "city")
    private List<Route> routes;


    public City(){

    }

    public City(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id && Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

}
