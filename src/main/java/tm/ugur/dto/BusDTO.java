package tm.ugur.dto;

public class BusDTO {

    private String carNumber;
    private String number;
    private String speed;
    private String imei;

    private String location;
    private String dir;
    private String lat;
    private String lng;

    public BusDTO(){}

    public BusDTO(String carNumber, String number, String speed, String imei, String dir, String lat, String lng) {
        this.carNumber = carNumber;
        this.number = number;
        this.speed = speed;
        this.imei = imei;
        this.dir = dir;
        this.lat = lat;
        this.lng = lng;
    }

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "BusDAO{" +
                "carNumber='" + carNumber + '\'' +
                ", number='" + number + '\'' +
                ", speed='" + speed + '\'' +
                ", imei='" + imei + '\'' +
                ", dir='" + dir + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
