package tm.ugur.util.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.geo.LineStringDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RouteMapper extends AbstractMapper<Route, RouteDTO> {


    private final ModelMapper modelMapper;
    private final StopRepository stopRepository;

    @Autowired
    public RouteMapper(ModelMapper modelMapper, StopRepository stopRepository){
        super(Route.class, RouteDTO.class);
        this.modelMapper = modelMapper;
        this.stopRepository = stopRepository;
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
        destination.setFrontLine(getLine(source, "front"));
        destination.setBackLine(getLine(source, "back"));
    }

    private List<Long> getStopIds(Route source, String dir){
        if(Objects.isNull(source) || Objects.isNull(source.getId()))
            return null;

        return  dir.equals("start") ? this.getStartStopIds(source) : this.getEndStopIds(source);
    }

    private List<Long> getStartStopIds(Route source) {
        return source.getStartStops().stream().map(Stop::getId).collect(Collectors.toList());
    }

    private List<Long> getEndStopIds(Route source) {
        return source.getEndStops().stream().map(Stop::getId).collect(Collectors.toList());
    }

    private LineStringDTO getLine(Route source, String line){

        if(Objects.isNull(source) || Objects.isNull(source.getFrontLine()))
            return null;

        LineString lineString = line.equals("front") ? source.getFrontLine() : source.getBackLine();
        Coordinate[] coordinates = lineString.getCoordinates();
        List<PointDTO> points = new ArrayList<>();

        for(Coordinate coordinate : coordinates){
            points.add(new PointDTO(coordinate.getX(), coordinate.getY()));
        }

        return new LineStringDTO(points);

    }


    @Override
    void mapSpecificFields(RouteDTO source, Route destination) {
        List<Long> startStopIds = source.getStartStopIds();
        List<Long> endsStopIds = source.getEndStopIds();

        List<Stop> startStops = stopRepository.findAllById(startStopIds);
        List<Stop> endStops = stopRepository.findAllById(endsStopIds);

        if (!startStops.isEmpty())
            destination.setStartStops(startStops);

        if (!endStops.isEmpty())
            destination.setStartStops(endStops);
    }
}
