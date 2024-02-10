package tm.ugur.models;

import jakarta.persistence.*;

@Entity
@Table(name = "end_route_stop")
public class EndRouteStop {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "stop_id", referencedColumnName = "id")
    private Stop stop;

    @Column(name = "index")
    private Integer index;

    public EndRouteStop(){

    }

    public EndRouteStop(Integer index) {
        this.index = index;
    }

    public EndRouteStop(Route route, Stop stop, Integer index){
        this.route = route;
        this.stop = stop;
        this.index = index;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "EndRouteStop{" +
                "id=" + id +
                ", index=" + index +
                '}';
    }
}
