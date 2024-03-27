package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.models.PlaceSubCategoryTranslation;

@Component
public class TranslationPlaceSubCategoryMapper extends AbstractMapper<PlaceSubCategoryTranslation, TranslationDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public TranslationPlaceSubCategoryMapper(ModelMapper modelMapper) {
        super(PlaceSubCategoryTranslation.class, TranslationDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlaceSubCategoryTranslation.class, TranslationDTO.class)
                .addMappings(m -> {
                    m.skip(TranslationDTO::setId);
                    m.skip(TranslationDTO::setAddress);
                }).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(TranslationDTO.class, PlaceSubCategoryTranslation.class)
                .addMappings(m -> {
                    m.skip(PlaceSubCategoryTranslation::setId);
                }).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(PlaceSubCategoryTranslation source, TranslationDTO destination) {
        destination.setTitle(source.getTitle());
        destination.setLocale(source.getLocale());
        destination.setId(source.getId());
    }


}
