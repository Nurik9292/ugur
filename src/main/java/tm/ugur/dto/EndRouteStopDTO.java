package tm.ugur.dto;

import java.io.Serializable;

public class EndRouteStopDTO extends AbstractDTO implements Serializable {

    private RouteDTO route;

    private StopDTO stop;

    private int index;

    public EndRouteStopDTO(){

    }

    public EndRouteStopDTO(RouteDTO route, StopDTO stop, int index){
        this.route = route;
        this.stop = stop;
        this.index = index;
    }


    public RouteDTO getRoute() {
        return route;
    }

    public void setRoute(RouteDTO route) {
        this.route = route;
    }

    public StopDTO getStop() {
        return stop;
    }

    public void setStop(StopDTO stop) {
        this.stop = stop;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
