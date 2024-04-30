package tm.ugur.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import tm.ugur.dto.geo.LineStringDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RouteDTO extends AbstractDTO implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "interval")
    private String interval;
    @Column(name = "number")
    private int number;

    @JsonIgnore
    private int routingTime;
    @Column(name = "city_id")
    private CityDTO city;
    private List<Long> startStopIds;
    private List<Long> endStopIds;
    private LineStringDTO frontLine;
    private LineStringDTO backLine;
    private transient Boolean is_favorite;


    public RouteDTO(){

    }

    public RouteDTO(long id, String name, String interval, int number, CityDTO city) {
        this.id = id;
        this.name = name;
        this.interval = interval;
        this.number = number;
        this.city = city;
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

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }


    public List<Long> getStartStopIds() {
        return startStopIds;
    }

    public void setStartStopIds(List<Long> startStopIds) {
        this.startStopIds = startStopIds;
    }

    public List<Long> getEndStopIds() {
        return endStopIds;
    }

    public void setEndStopIds(List<Long> endStopIds) {
        this.endStopIds = endStopIds;
    }

    public LineStringDTO getFrontLine() {
        return frontLine;
    }

    public void setFrontLine(LineStringDTO frontLine) {
        this.frontLine = frontLine;
    }

    public LineStringDTO getBackLine() {
        return backLine;
    }

    public void setBackLine(LineStringDTO backLine) {
        this.backLine = backLine;
    }

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Boolean favorite) {
        is_favorite = favorite;
    }

    public int getRoutingTime() {
        return routingTime;
    }

    public void setRoutingTime(int routingTime) {
        this.routingTime = routingTime;
    }


    @Override
    public String toString() {
        return "RouteDTO{" +
                "name='" + name + '\'' +
                ", interval='" + interval + '\'' +
                ", number=" + number +
                ", city=" + city +
                ", frontLine=" + frontLine +
                ", backLine=" + backLine +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RouteDTO routeDTO = (RouteDTO) object;
        return number == routeDTO.number && routingTime == routeDTO.routingTime && Objects.equals(name, routeDTO.name) && Objects.equals(interval, routeDTO.interval) && Objects.equals(is_favorite, routeDTO.is_favorite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, interval, number, routingTime, is_favorite);
    }
}
