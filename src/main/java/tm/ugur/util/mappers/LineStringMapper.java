package tm.ugur.util.mappers;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.geo.LineStringDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class LineStringMapper {

    private final GeometryFactory factory;

    @Autowired
    public LineStringMapper(GeometryFactory factory) {
        this.factory = factory;
    }


    public LineStringDTO getLineDto(Route source, String line){
        if (this.isNullLine(source, line))
            return null;

        LineString lineString = line.equals("front") ? source.getFrontLine() : source.getBackLine();
        Coordinate[] coordinates = lineString.getCoordinates();
        List<PointDTO> points = new ArrayList<>();

        for (Coordinate coordinate : coordinates) {
            points.add(new PointDTO(coordinate.getX(), coordinate.getY()));
        }

        return new LineStringDTO(points);

    }


    public LineString getLine(RouteDTO source, String line){
        if(this.isNullLine(source, line))
            return null;

        LineStringDTO lineStringDTO = line.equals("front") ? source.getFrontLine() : source.getBackLine();
        List<PointDTO> points = lineStringDTO.getCoordinates();
        Coordinate[] coordinates = points.stream().map(point
                -> new Coordinate(point.getLat(), point.getLng())).toArray(Coordinate[]::new);

        return this.factory.createLineString(coordinates);
    }

    private boolean isNullLine(Route source, String line) {
        return Objects.isNull(source) ||
                (line.equals("front") && Objects.isNull(source.getFrontLine())) ||
                (line.equals("back") && Objects.isNull(source.getBackLine()));
    }

    private boolean isNullLine(RouteDTO source, String line) {
        return Objects.isNull(source) ||
                (line.equals("front") && Objects.isNull(source.getFrontLine())) ||
                (line.equals("back") && Objects.isNull(source.getBackLine()));
    }
}
