package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Stop;


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
                .addMappings(m -> m.skip(StopDTO::setLocation)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(StopDTO.class, Stop.class)
                .addMappings(m -> {
                    m.skip(Stop::setLocation);
                    m.skip(Stop::setLat);
                    m.skip(Stop::setLng);
                }).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(Stop source, StopDTO destination) {
        destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
    }


    @Override
    public void mapSpecificFields(StopDTO source, Stop destination){
        destination.setLocation(
                this.factory.createPoint(
                        new Coordinate(source.getLocation().getLat(), source.getLocation().getLng())));

    }
}
