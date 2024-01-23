package tm.ugur.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.locationtech.jts.geom.Point;
import tm.ugur.models.City;

public class StopDTO {

    @Column(name = "id")
    private int id;
    @NotEmpty(message = "Заполните название.")
    @Size(min = 3, max = 10, message = "Назвние города должен состоять от 3 до 10 символов.")
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private Point location;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    private int cityId;

    public StopDTO(){}

    public StopDTO(String name, Point location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public City getCity() {
//        return city;
//    }

    public void setCity(City city) {
        this.setCityId(city.getId());
        this.city = city;
    }

    public int getCityId(){
        return this.cityId;
    }

    public void setCityId(int cityId){
        this.cityId = cityId;
    }
}
