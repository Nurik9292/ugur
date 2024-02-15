package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.dto.geo.LineStringDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class RouteMapper extends AbstractMapper<Route, RouteDTO> {


    private final ModelMapper modelMapper;
    private final GeometryFactory factory;
    private final StopService stopService;



    @Autowired
    public RouteMapper(ModelMapper modelMapper, GeometryFactory factory, StopService stopService){
        super(Route.class, RouteDTO.class);
        this.modelMapper = modelMapper;
        this.factory = factory;
        this.stopService = stopService;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(Route.class, RouteDTO.class)
                .addMappings(m -> m.skip(RouteDTO::setStartStopIds)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(RouteDTO.class, Route.class)
                .addMappings(m -> m.skip(Route::setStartStops)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Route source, RouteDTO destination) {
        destination.setStartStopIds(getStopIds(source, "start"));
        destination.setEndStopIds(getStopIds(source, "end"));
        destination.setFrontLine(getLineDto(source, "front"));
        destination.setBackLine(getLineDto(source, "back"));
    }


    protected List<Long> getStopIds(Route source, String line) {

        if (Objects.isNull(source) || Objects.isNull(source.getId()) || Objects.isNull(source.getStartStops())) {
            return null;
        }
        List<Stop> stops = line.equals("start") ? source.getStartStops() : source.getEndStops();
            if (isNull(stops)) {
                return null;
        }

        return stops.stream().map(Stop::getId).collect(Collectors.toList());
    }


    private LineStringDTO getLineDto(Route source, String line){
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


    @Override
    public void mapSpecificFields(RouteDTO source, Route destination) {
        destination.setStartStops(getStops(source, "start"));
        destination.setEndStops(getStops(source, "back"));
        destination.setFrontLine(getLine(source, "front"));
        destination.setBackLine(getLine(source, "back"));
    }


    private List<Stop> getStops(RouteDTO source, String line){
        if (Objects.isNull(source) || Objects.isNull(source.getId()) || Objects.isNull(source.getStartStopIds())) {
            return null;
        }

        List<Long> ids = line.equals("start") ? source.getStartStopIds() : source.getEndStopIds();

        return ids.stream().map(stopService::findOne).collect(Collectors.toList());
    }


    private LineString getLine(RouteDTO source, String line){
        if(this.isNullLine(source, line))
            return null;

        LineStringDTO lineStringDTO = line.equals("front") ? source.getFrontLine() : source.getBackLine();
        List<PointDTO> points = lineStringDTO.getCoordinates();
        Coordinate[] coordinates = points.stream().map(point
                -> new Coordinate(point.getLat(), point.getLng())).toArray(Coordinate[]::new);

        return this.factory.createLineString(coordinates);
    }


    private boolean isNull(List<Stop> stops){
        return Objects.isNull(stops);
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
