package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.PlacePhoneDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.PlacePhone;

@Component
public class PlacePhoneMapper extends AbstractMapper<PlacePhone, PlacePhoneDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public PlacePhoneMapper(ModelMapper modelMapper) {
        super(PlacePhone.class, PlacePhoneDTO.class);
        this.modelMapper = modelMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlacePhone.class, PlacePhoneDTO.class)
                .addMappings(m -> m.skip(PlacePhoneDTO::setType)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(PlacePhoneDTO.class, PlacePhone.class)
                .addMappings(m -> m.skip(PlacePhone::setType)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(PlacePhone source, PlacePhoneDTO destination){
        destination.setId(source.getId());
        destination.setNumber(source.getNumber());
        destination.setType(source.getType());
    }
}
