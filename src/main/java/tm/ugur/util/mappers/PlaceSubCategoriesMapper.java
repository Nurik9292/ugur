package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlaceSubCategoriesMapper extends AbstractMapper<PlaceSubCategory, PlaceSubCategoryDTO>{


    private final ModelMapper modelMapper;
    private final TranslationPlaceSubCategoryMapper translationMapper;

    @Autowired
    public PlaceSubCategoriesMapper(ModelMapper modelMapper,
                                    TranslationPlaceSubCategoryMapper translationMapper) {
        super(PlaceSubCategory.class, PlaceSubCategoryDTO.class);
        this.modelMapper = modelMapper;
        this.translationMapper = translationMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlaceSubCategory.class, PlaceSubCategoryDTO.class)
                .addMappings(m -> m.skip(PlaceSubCategoryDTO::setId)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(PlaceSubCategoryDTO.class, PlaceSubCategory.class)
                .addMappings(m -> m.skip(PlaceSubCategory::setId)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(PlaceSubCategory source, PlaceSubCategoryDTO destination) {
        destination.setId(source.getId());
        destination.setTitles(getTitleTranslations(source.getTranslations()));
    }


    private Map<String, String> getTitleTranslations(List<PlaceSubCategoryTranslation> translations){
        return translations.stream()
                .collect(Collectors.toMap(PlaceSubCategoryTranslation::getLocale, PlaceSubCategoryTranslation::getTitle));
    }
}
