package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.locationtech.jts.geom.LineString;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "routes")
public class Route extends AbstractEntity{

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
    @JoinTable(name = "route_favorites",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients;

    @ManyToMany
    @JoinTable(name = "end_route_stop",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "stop_id"))
    private List<Stop> endStops;

    @OneToMany(mappedBy = "route")
    private List<StartRouteStop> startRouteStops;

    @OneToMany(mappedBy = "route")
    private List<EndRouteStop> endRouteStops;


    @Column(name = "front_line")
    private LineString frontLine;

    @Column(name = "back_line")
    private LineString backLine;

    public Route(){}

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Stop> getStartStops() {
        return startStops;
    }

    public void setStartStops(List<Stop> startStops) {
        this.startStops = startStops;
    }

    public List<Stop> getEndStops() {
        return endStops;
    }

    public void setEndStops(List<Stop> endStops) {
        this.endStops = endStops;
    }

    public List<StartRouteStop> getStartRouteStops() {
        return startRouteStops;
    }

    public void setStartRouteStops(List<StartRouteStop> startRouteStops) {
        this.startRouteStops = startRouteStops;
    }

    public List<EndRouteStop> getEndRouteStops() {
        return endRouteStops;
    }

    public void setEndRouteStops(List<EndRouteStop> endRouteStops) {
        this.endRouteStops = endRouteStops;
    }

    public LineString getBackLine() {
        return backLine;
    }

    public void setBackLine(LineString backLine) {
        this.backLine = backLine;
    }

    public LineString getFrontLine() {
        return frontLine;
    }

    public void setFrontLine(LineString frontLine) {
        this.frontLine = frontLine;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && number == route.number && Objects.equals(name, route.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number);
    }


}


