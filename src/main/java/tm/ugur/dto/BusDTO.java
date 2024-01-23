package tm.ugur.dto;

import java.util.List;

public class BusDTO extends  AbstractDTO{

    private String carNumber;
    private String number;
    private String speed;
    private String imei;
    private String dir;
    private List<Double> location;


    public BusDTO(){}


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }
}
