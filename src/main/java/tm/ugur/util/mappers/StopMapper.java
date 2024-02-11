package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class StopMapper extends AbstractMapper<Stop, StopDTO>{

    private final ModelMapper modelMapper;
    private final GeometryFactory factory;

    @Autowired
    public StopMapper(ModelMapper modelMapper, GeometryFactory factory) {
        super(Stop.class, StopDTO.class);
        this.modelMapper = modelMapper;
        this.factory = factory;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(Stop.class, StopDTO.class)
                .addMappings(m -> {
                    m.skip(StopDTO::setLocation);
                    m.skip(StopDTO::setEndRouteIds);
                    m.skip(StopDTO::setStartRouteIds    );
                }).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(StopDTO.class, Stop.class)
                .addMappings(m -> {
                    m.skip(Stop::setLocation);
                    m.skip(Stop::setLat);
                    m.skip(Stop::setLng);
                }).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(Stop source, StopDTO destination) {
        destination.setStartRouteIds(getStopIds(source, "start"));
        destination.setEndRouteIds(getStopIds(source, "end"));
        destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
    }


    private List<Long> getStopIds(Stop source, String line) {
        if (Objects.isNull(source) || Objects.isNull(source.getId()) || Objects.isNull(source.getStartRoutes())) {
            return null;
        }

        List<Route> routes = line.equals("start") ? source.getStartRoutes() : source.getEndRoutes();
        if (isNull(routes)) {
            return null;
        }

        return routes.stream().map(Route::getId).collect(Collectors.toList());
    }


    @Override
    public void mapSpecificFields(StopDTO source, Stop destination){
        destination.setLocation(
                this.factory.createPoint(
                        new Coordinate(source.getLocation().getLat(), source.getLocation().getLng())));

    }

    private boolean isNull(List<Route> routes){
        return Objects.isNull(routes);
    }
}
