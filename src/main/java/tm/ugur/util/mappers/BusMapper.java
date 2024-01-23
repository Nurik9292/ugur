package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Bus;

@Component
public class BusMapper extends AbstractMapper<Bus, BusDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public BusMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper(){
        this.modelMapper.createTypeMap(Bus.class, BusDTO.class)
                .addMappings(m -> m.skip(BusDTO::setId)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(BusDTO.class, Bus.class)
                .addMappings(m -> m.skip(Bus::setId)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(Bus source, BusDTO destination) {
        destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
    }
}
