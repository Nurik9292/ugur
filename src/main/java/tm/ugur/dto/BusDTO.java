package tm.ugur.dto;

import tm.ugur.dto.geo.PointDTO;

import java.io.Serializable;
import java.util.Objects;


public class BusDTO extends AbstractDTO implements Serializable {

    private String carNumber;
    private Integer number;
    private String speed;
    private String dir;
    private PointDTO location;
    private Integer index;
    private String side;
    private boolean status;

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BusDAO{" +
                "carNumber='" + carNumber + '\'' +
                ", number='" + number + '\'' +
                ", speed='" + speed + '\'' +
                ", dir='" + dir + '\'' +
                ", side='" + side + '\'' +
                ", index='" + index + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BusDTO busDTO = (BusDTO) object;
        return status == busDTO.status && Objects.equals(carNumber, busDTO.carNumber) && Objects.equals(number, busDTO.number) && Objects.equals(speed, busDTO.speed) && Objects.equals(dir, busDTO.dir) && Objects.equals(index, busDTO.index) && Objects.equals(side, busDTO.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carNumber, number, speed, dir, index, side, status);
    }
}
