package tm.ugur.dto;


import tm.ugur.dto.geo.PointDTO;

import java.util.List;

public class StopDTO extends AbstractDTO{

    private String name;
    private CityDTO city;
    private PointDTO location;

    List<Long> startRouteIds;

    List<Long> endRouteIds;

    private Boolean isFavorite;



    public StopDTO(){}

    public StopDTO(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public PointDTO getLocation() {
        return location;
    }

    public void setLocation(PointDTO location) {
        this.location = location;
    }

    public Boolean getIs_favorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public List<Long> getStartRouteIds() {
        return startRouteIds;
    }

    public void setStartRouteIds(List<Long> startRouteIds) {
        this.startRouteIds = startRouteIds;
    }

    public List<Long> getEndRouteIds() {
        return endRouteIds;
    }

    public void setEndRouteIds(List<Long> endRouteIds) {
        this.endRouteIds = endRouteIds;
    }


    @Override
    public String toString() {
        return "StopDTO{" +
                "name='" + name + '\'' +
                ", city=" + city +
                ", location=" + location +
                '}';
    }


}
