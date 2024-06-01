package tm.ugur.util.hopper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteResponse {

    private Hints hints;
    private Info info;
    private List<Path> paths;

    public Hints getHints() {
        return hints;
    }

    public void setHints(Hints hints) {
        this.hints = hints;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }


    public static class Hints {
        private int visitedNodesSum;
        private int visitedNodesAverage;

        @JsonProperty("visited_nodes.sum")
        public int getVisitedNodesSum() {
            return visitedNodesSum;
        }

        public void setVisitedNodesSum(int visitedNodesSum) {
            this.visitedNodesSum = visitedNodesSum;
        }

        @JsonProperty("visited_nodes.average")
        public int getVisitedNodesAverage() {
            return visitedNodesAverage;
        }

        public void setVisitedNodesAverage(int visitedNodesAverage) {
            this.visitedNodesAverage = visitedNodesAverage;
        }

        @Override
        public String toString() {
            return "Hints{" +
                    "visitedNodesSum=" + visitedNodesSum +
                    ", visitedNodesAverage=" + visitedNodesAverage +
                    '}';
        }
    }

    public static class Info {
        private List<String> copyrights;
        private int took;

        public List<String> getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(List<String> copyrights) {
            this.copyrights = copyrights;
        }

        public int getTook() {
            return took;
        }

        public void setTook(int took) {
            this.took = took;
        }

        @Override
       public String toString() {
           return "Info{" +
                   "copyrights=" + copyrights +
                   ", took=" + took +
                   '}';
       }

    }

    public static class Path {
        private double distance;
        private double weight;
        private long time;
        private int transfers;
        private boolean pointsEncoded;
        private List<Double> bbox;
        private Points points;
        private List<Instruction> instructions;
        private List<Object> legs;
        private Map<String, Object> details;
        private double ascend;
        private double descend;
        private Map<String, Object> snappedWaypoints;

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTransfers() {
            return transfers;
        }

        public void setTransfers(int transfers) {
            this.transfers = transfers;
        }

        public boolean isPointsEncoded() {
            return pointsEncoded;
        }

        public void setPointsEncoded(boolean pointsEncoded) {
            this.pointsEncoded = pointsEncoded;
        }

        public List<Double> getBbox() {
            return bbox;
        }

        public void setBbox(List<Double> bbox) {
            this.bbox = bbox;
        }

        public Points getPoints() {
            return points;
        }

        public void setPoints(Points points) {
            this.points = points;
        }

        public List<Instruction> getInstructions() {
            return instructions;
        }

        public void setInstructions(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public List<Object> getLegs() {
            return legs;
        }

        public void setLegs(List<Object> legs) {
            this.legs = legs;
        }

        public Map<String, Object> getDetails() {
            return details;
        }

        public void setDetails(Map<String, Object> details) {
            this.details = details;
        }

        public double getAscend() {
            return ascend;
        }

        public void setAscend(double ascend) {
            this.ascend = ascend;
        }

        public double getDescend() {
            return descend;
        }

        public void setDescend(double descend) {
            this.descend = descend;
        }

        public Map<String, Object> getSnappedWaypoints() {
            return snappedWaypoints;
        }

        public void setSnappedWaypoints(Map<String, Object> snappedWaypoints) {
            this.snappedWaypoints = snappedWaypoints;
        }

        @Override
        public String toString() {
            return "Path{" +
                    "distance=" + distance +
                    ", weight=" + weight +
                    ", time=" + time +
                    ", transfers=" + transfers +
                    ", pointsEncoded=" + pointsEncoded +
                    ", bbox=" + bbox +
                    ", points=" + points +
                    ", instructions=" + instructions +
                    ", legs=" + legs +
                    ", details=" + details +
                    ", ascend=" + ascend +
                    ", descend=" + descend +
                    ", snappedWaypoints=" + snappedWaypoints +
                    '}';
        }
    }

    public static class Points {
        private List<List<Double>> coordinates;
        private String type;

        public List<List<Double>> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<List<Double>> coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Points{" +
                    "coordinates=" + coordinates +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public static class Instruction {
        private String text;
        private double distance;
        private double time;
        private int interval[];

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public int[] getInterval() {
            return interval;
        }

        public void setInterval(int[] interval) {
            this.interval = interval;
        }

        @Override
        public String toString() {
            return "Instruction{" +
                    "text='" + text + '\'' +
                    ", distance=" + distance +
                    ", time=" + time +
                    ", interval=" + Arrays.toString(interval) +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RouteResponse{" +
                "hints=" + hints +
                ", info=" + info +
                ", paths=" + paths +
                '}';
    }
}
