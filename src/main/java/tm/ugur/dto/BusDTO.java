package tm.ugur.dto;

import tm.ugur.dto.geo.PointDTO;

import java.util.List;

public class BusDTO extends AbstractDTO{

    private String carNumber;
    private Integer number;
    private String speed;
    private String imei;
    private String dir;
    private PointDTO location;


    public BusDTO(){}


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public PointDTO getLocation() {
        return location;
    }

    public void setLocation(PointDTO location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "BusDAO{" +
                "carNumber='" + carNumber + '\'' +
                ", number='" + number + '\'' +
                ", speed='" + speed + '\'' +
                ", imei='" + imei + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }

}
