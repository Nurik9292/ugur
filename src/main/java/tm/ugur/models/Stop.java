package tm.ugur.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stops")
public class Stop extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotEmpty(message = "Заполните название.")
    @Column(name = "name")
    private String name;


    @Column(name = "location", columnDefinition = "Geometry(Point, 4326)")
    private Point location;


    @ManyToOne
    private City city;


    @ManyToMany(mappedBy = "startStops")
    List<Route> startRoutes;

    @ManyToMany(mappedBy = "endStops")
    List<Route> endRoutes;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Min(value = 0, message = "Заполните поле")
    @Transient
    private Double lat;

    @Min(value = 0, message = "Заполните поле")
    @Transient
    private Double lng;

    @OneToMany(mappedBy = "stop")
    private List<StartRouteStop> startRouteStops;

    @OneToMany(mappedBy = "stop")
    private List<EndRouteStop> endRouteStops;

    @ManyToMany
    @JoinTable(name = "stop_favorites",
            joinColumns = @JoinColumn(name = "stop_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients;

    public Stop(){

    }

    public Stop(String name) {
        this.name = name;
    }

    public Stop(Long id, String name, Point location, City city){
        this.id = id;
        this.name = name;
        this.location = location;
        this.city = city;
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
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

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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
