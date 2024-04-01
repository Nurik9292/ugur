package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class RouteMapper extends AbstractMapper<Route, RouteDTO> {


    private final ModelMapper modelMapper;
    private final StopService stopService;
    private final LineStringMapper lineStringMapper;


    @Autowired
    public RouteMapper(ModelMapper modelMapper,
                       StopService stopService,
                       LineStringMapper lineStringMapper){
        super(Route.class, RouteDTO.class);
        this.modelMapper = modelMapper;
        this.stopService = stopService;
        this.lineStringMapper = lineStringMapper;
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
        destination.setFrontLine(lineStringMapper.getLineDto(source, "front"));
        destination.setBackLine(lineStringMapper.getLineDto(source, "back"));
    }

    @Override
    public void mapSpecificFields(RouteDTO source, Route destination) {
        destination.setStartStops(getStops(source, "start"));
        destination.setEndStops(getStops(source, "end"));
        destination.setFrontLine(lineStringMapper.getLine(source, "front"));
        destination.setBackLine(lineStringMapper.getLine(source, "back"));
    }


    protected List<Long> getStopIds(Route source, String line) {

        if (isNullLine(source, line)) {
            return null;
        }
        List<Stop> stops = line.equals("start") ? source.getStartRouteStops()
                .stream()
                .sorted(Comparator.comparing(StartRouteStop::getIndex))
                .toList()
                .stream()
                .map(StartRouteStop::getStop)
                .toList() : source.getEndRouteStops()
                .stream()
                .sorted(Comparator.comparing(EndRouteStop::getIndex))
                .toList()
                .stream()
                .map(EndRouteStop::getStop)
                .toList();

        return stops.stream().map(Stop::getId).collect(Collectors.toList());
    }

    private List<Stop> getStops(RouteDTO source, String line){
        if (Objects.isNull(source) || Objects.isNull(source.getId()) || Objects.isNull(source.getStartStopIds())) {
            return null;
        }

        List<Long> ids = line.equals("start") ? source.getStartStopIds() : source.getEndStopIds();

        return ids.stream().map(stopService::findOne).collect(Collectors.toList());
    }


    private boolean isNull(List<?> objects){
        return Objects.isNull(objects);
    }


    private boolean isNullLine(Route source, String line) {
        return Objects.isNull(source) ||
                (line.equals("front") && Objects.isNull(source.getFrontLine())) ||
                (line.equals("back") && Objects.isNull(source.getBackLine()));
    }

}
