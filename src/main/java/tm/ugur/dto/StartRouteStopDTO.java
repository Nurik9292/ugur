package tm.ugur.dto;

public class StartRouteStopDTO extends AbstractDTO{

    private RouteDTO route;

    private StopDTO stop;

    private Integer index;

    public StartRouteStopDTO(){

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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
