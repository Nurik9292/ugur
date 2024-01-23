package tm.ugur.dto.geo;

import java.util.List;

public class LineStringDTO {

    private List<PointDTO> coordinates;

    public LineStringDTO(){}


    public LineStringDTO(List<PointDTO> coordinates) {
        this.coordinates = coordinates;
    }

    public List<PointDTO> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<PointDTO> coordinates) {
        this.coordinates = coordinates;
    }
}
