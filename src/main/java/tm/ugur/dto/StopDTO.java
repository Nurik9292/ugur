package tm.ugur.dto;


import tm.ugur.dto.geo.PointDTO;

public class StopDTO extends AbstractDTO{

    private String name;
    private CityDTO city;
    private PointDTO location;




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

    @Override
    public String toString() {
        return "StopDTO{" +
                "name='" + name + '\'' +
                ", city=" + city +
                ", location=" + location +
                '}';
    }
}
