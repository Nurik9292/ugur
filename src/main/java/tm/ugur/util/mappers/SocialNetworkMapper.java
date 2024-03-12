package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.SocialNetworkDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.SocialNetwork;

@Component
public class SocialNetworkMapper extends AbstractMapper<SocialNetwork, SocialNetworkDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public SocialNetworkMapper(ModelMapper modelMapper) {
        super(SocialNetwork.class, SocialNetworkDTO.class);
        this.modelMapper = modelMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(SocialNetwork.class, SocialNetworkDTO.class)
                .addMappings(m -> m.skip(SocialNetworkDTO::setId)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(SocialNetworkDTO.class, SocialNetwork.class)
                .addMappings(m -> m.skip(SocialNetwork::setId)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(SocialNetwork source, SocialNetworkDTO destination) {
        destination.setId(source.getId());
        destination.setLink(source.getLink());
    }

}
