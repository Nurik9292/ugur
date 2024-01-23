package tm.ugur.models;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Entity
@Table(name = "buses")
public class Bus extends AbstractEntity{


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "car_number")
    private String carNumber;

    @Column(name = "location")
    private Point location;

    @Column(name = "speed")
    private String speed;

    @Column(name = "imei")
    private String imei;

    @Column(name = "number")
    private Integer number;

    @Column(name = "dir")
    private String dir;



    public Bus(){}

    public Bus(String carNumber, Integer number, String speed, String imei, String dir, String lat, String lng) {
        this.carNumber = carNumber;
        this.number = number;
        this.speed = speed;
        this.imei = imei;
        this.dir = dir;

        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        this.location = factory.createPoint(new Coordinate(Double.parseDouble(lat), Double.parseDouble(lng)));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", carNumber='" + carNumber + '\'' +
                '}';
    }

}
