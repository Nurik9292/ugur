package tm.ugur.util.pojo;

import java.util.Map;

public class RouteStopStart {

    private String name;

    private Map<String, Integer> stopIndex;

    public RouteStopStart() {
    }

    public RouteStopStart(String name, Map<String, Integer> stopIndex) {
        this.name = name;
        this.stopIndex = stopIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getStopIndex() {
        return stopIndex;
    }

    public void setStopIndex(Map<String, Integer> stopIndex) {
        this.stopIndex = stopIndex;
    }
}
