package tm.ugur.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import tm.ugur.models.City;

public class StopDTO {

    @Column(name = "id")
    private int id;
    @NotEmpty(message = "Заполните название.")
    @Size(min = 3, max = 10, message = "Назвние города должен состоять от 3 до 10 символов.")
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @ManyToOne
    private City city;

    public StopDTO(){}

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

    @Override
    public String toString() {
        return "StopDTO{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", city=" + city +
                '}';
    }
}
