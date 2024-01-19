package tm.ugur.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import tm.ugur.models.City;

public class RouteDTO {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    @NotEmpty(message = "Заполните поле.")
    @Size(min = 3, max = 70, message = "Назвние маршрута должен состоять от 3 до 100 символов.")
    private String name;

    @Column(name = "interval")
    private String interval;

    @Column(name = "number")
    @Min(value = 1, message = "Номер маршрута не может быть меньше 1")
    private int number;

    @Column(name = "routing_time")
    private int routeTime;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    private int cityId;

    @Column(name = "front_line")
    private String front_line;

    @Column(name = "back_line")
    private String back_line;

    public RouteDTO(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRouteTime() {
        return routeTime;
    }

    public void setRouteTime(int routeTime) {
        this.routeTime = routeTime;
    }

//    public City getCity() {
//        return city;
//    }

    public void setCity(City city) {
        this.city = city;
        this.cityId = city.getId();
    }

    public String getFront_line() {
        return front_line;
    }

    public void setFront_line(String front_line) {
        this.front_line = front_line;
    }

    public String getBack_line() {
        return back_line;
    }

    public void setBack_line(String back_line) {
        this.back_line = back_line;
    }

    public void setCityId(int cityId){
        this.cityId = cityId;
    }

    public int getCityId(){
        return this.cityId;
    }
}
