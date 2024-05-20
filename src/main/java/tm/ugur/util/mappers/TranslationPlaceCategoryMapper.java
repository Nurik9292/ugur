package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.models.place.category.PlaceCategoryTranslation;

@Component
public class TranslationPlaceCategoryMapper extends AbstractMapper<PlaceCategoryTranslation, TranslationDTO>{

    private final ModelMapper modelMapper;

    @Autowired
    public TranslationPlaceCategoryMapper(ModelMapper modelMapper) {
        super(PlaceCategoryTranslation.class, TranslationDTO.class);
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlaceCategoryTranslation.class, TranslationDTO.class)
                .addMappings(m -> {
                    m.skip(TranslationDTO::setId);
                    m.skip(TranslationDTO::setAddress);
                }).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(TranslationDTO.class, PlaceCategoryTranslation.class)
                .addMappings(m -> {
                    m.skip(PlaceCategoryTranslation::setId);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(PlaceCategoryTranslation source, TranslationDTO destination) {
        destination.setTitle(source.getTitle());
        destination.setLocale(source.getLocale());
        destination.setId(source.getId());
    }
}
