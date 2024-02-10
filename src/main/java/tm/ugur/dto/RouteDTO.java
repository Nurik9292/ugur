package tm.ugur.dto;

import jakarta.persistence.Column;
import tm.ugur.dto.geo.LineStringDTO;

import java.util.List;

public class RouteDTO extends AbstractDTO{

    @Column(name = "name")
    private String name;

    @Column(name = "interval")
    private String interval;

    @Column(name = "number")
    private int number;

    @Column(name = "city_id")
    private CityDTO city;

    private List<Long> startStopIds;

    private List<Long> endStopIds;

    private LineStringDTO frontLine;

    private LineStringDTO backLine;

    private Boolean isFavorite;


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

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
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


}
