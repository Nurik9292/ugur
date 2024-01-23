package tm.ugur.dto;


import tm.ugur.models.Route;

import java.util.List;

public class StopDTO extends AbstractDTO{

    private String name;

    private CityDTO city;

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
}
