package tm.ugur.dto;

import tm.ugur.dto.geo.PointDTO;

import java.util.List;

public class BusDTO extends AbstractDTO{

    private String carNumber;
    private Integer number;
    private String speed;
    private String dir;
    private PointDTO location;


    public BusDTO(){}

    public BusDTO(String carNumber, Integer number){
        this.carNumber = carNumber;
        this.number = number;
    }

    public BusDTO(String speed, String dir, String lat, String lng){
        this.speed = speed;
        this.dir = dir;
        this.location = new PointDTO(Double.parseDouble(lat), Double.parseDouble(lng));
    }


    public BusDTO(String carNumber, String speed, String dir, String lat, String lng){
        this.carNumber = carNumber;
        this.speed = speed;
        this.dir = dir;
        this.location = new PointDTO(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    public BusDTO(Long id, String carNumber, Integer number, String speed, String dir, PointDTO location){
        this.id = id;
        this.carNumber = carNumber;
        this.number = number;
        this.speed = speed;
        this.dir = dir;
        this.location = location;
    }

    public BusDTO(String carNumber, Integer number, String speed, String dir, String lat, String lng){
        this.carNumber = carNumber;
        this.number = number;
        this.speed = speed;
        this.dir = dir;
        this.location = new PointDTO(Double.parseDouble(lat), Double.parseDouble(lng));
    }


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
                ", dir='" + dir + '\'' +
                '}';
    }

}
