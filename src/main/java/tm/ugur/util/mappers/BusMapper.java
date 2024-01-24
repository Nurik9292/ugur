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
        super(Bus.class, BusDTO.class);
        this.modelMapper = modelMapper;
    }


    @Override
    public void mapSpecificFields(Bus source, BusDTO destination) {
        destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
    }
}
