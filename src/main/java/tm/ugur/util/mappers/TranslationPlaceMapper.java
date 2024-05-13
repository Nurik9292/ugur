package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.models.PlaceTranslation;
import tm.ugur.models.Route;

@Component
public class TranslationPlaceMapper extends AbstractMapper<PlaceTranslation, TranslationDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public TranslationPlaceMapper(ModelMapper modelMapper) {
        super(PlaceTranslation.class, TranslationDTO.class);
        this.modelMapper = modelMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlaceTranslation.class, TranslationDTO.class)
                .addMappings(m -> {
                    m.skip(TranslationDTO::setId);
                    m.skip(TranslationDTO::setAddress);
                    m.skip(TranslationDTO::setTitle);
                }).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(TranslationDTO.class, PlaceTranslation.class)
                .addMappings(m -> {
                    m.skip(PlaceTranslation::setId);
                }).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(PlaceTranslation source, TranslationDTO destination) {
        destination.setTitle(source.getTitle());
        destination.setAddress(source.getAddress());
        destination.setLocale(source.getLocale());
        destination.setId(source.getId());
    }

    @Override
    public void mapSpecificFields(TranslationDTO source, PlaceTranslation destination) {
        destination.setTitle(source.getTitle());
        destination.setAddress(source.getAddress());
        destination.setLocale(source.getLocale());
    }
}
