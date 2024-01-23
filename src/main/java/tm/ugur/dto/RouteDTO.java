package tm.ugur.dto;

import java.util.List;

public class RouteDTO extends AbstractDTO{

    private String name;

    private String interval;

    private int number;

    private CityDTO city;

    private List<Long> startStopIds;

    private List<Long> endStopIds;

    private List<Double[]> frontLine;

    private List<Double[]> backLine;


    public RouteDTO(){

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

    public List<Double[]> getFrontLine() {
        return frontLine;
    }

    public void setFrontLine(List<Double[]> frontLine) {
        this.frontLine = frontLine;
    }

    public List<Double[]> getBackLine() {
        return backLine;
    }

    public void setBackLine(List<Double[]> backLine) {
        this.backLine = backLine;
    }
}
