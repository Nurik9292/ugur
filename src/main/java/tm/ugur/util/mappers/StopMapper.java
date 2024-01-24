package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Stop;


@Component
public class StopMapper extends AbstractMapper<Stop, StopDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public StopMapper(ModelMapper modelMapper) {
        super(Stop.class, StopDTO.class);
        this.modelMapper = modelMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(Stop.class, StopDTO.class)
                .addMappings(m -> m.skip(StopDTO::setLocation)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(StopDTO.class, Stop.class)
                .addMappings(m -> m.skip(Stop::setLocation)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(Stop source, StopDTO destination) {
        destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
    }
}
