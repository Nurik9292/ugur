package tm.ugur.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.locationtech.jts.geom.LineString;
import tm.ugur.models.City;
import tm.ugur.models.Stop;

import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "start_route_stop",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "stop_id"))
    private List<Stop> startStops;

    @ManyToMany
    @JoinTable(name = "end_route_stop",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "stop_id"))
    private List<Stop> endStops;

    private List<Integer> startStopIds;

    private List<Integer> endStopIds;

    private int cityId;


    @Column(name = "front_line")
    private LineString frontLine;

    @Column(name = "back_line")
    private LineString back_line;

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

    public LineString getFrontLine() {
        return frontLine;
    }

    public void setFrontLine(LineString frontLine) {
        this.frontLine = frontLine;
    }

    public LineString getBackLine() {
        return back_line;
    }

    public void setBackLine(LineString backLine) {
        this.back_line = backLine;
    }

    public void setCityId(int cityId){
        this.cityId = cityId;
    }

    public int getCityId(){
        return this.cityId;
    }

//    public List<Stop> getStartStops() {
//        return startStops;
//    }

    public void setStartStops(List<Stop> startStops) {
        startStops.forEach(stop -> this.startStopIds.add(stop.getId()));
        this.startStops = startStops;
    }


//    public List<Stop> getEndStops() {
//        return endStops;
//    }

    public void setEndStops(List<Stop> endStops) {
        endStops.forEach(stop -> this.endStopIds.add(stop.getId()));
        this.endStops = endStops;
    }
}
