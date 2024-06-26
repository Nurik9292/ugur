package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.category.PlaceCategoryTranslation;
import tm.ugur.models.place.subCategory.PlaceSubCategory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PlaceCategoryMapper extends AbstractMapper<PlaceCategory, PlaceCategoryDTO> {


    private final ModelMapper modelMapper;
    private final PlaceSubCategoriesMapper placeSubCategoriesMapper;


    @Autowired
    public PlaceCategoryMapper(ModelMapper modelMapper,
                               PlaceSubCategoriesMapper placeSubCategoriesMapper) {
        super(PlaceCategory.class, PlaceCategoryDTO.class);
        this.modelMapper = modelMapper;
        this.placeSubCategoriesMapper = placeSubCategoriesMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(PlaceCategory.class, PlaceCategoryDTO.class)
                .addMappings(m -> m.skip(PlaceCategoryDTO::setSubCategories)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(PlaceCategoryDTO.class, PlaceCategory.class)
                .addMappings(m -> m.skip(PlaceCategory::setSubCategories)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(PlaceCategory source, PlaceCategoryDTO destination) {
            destination.setId(source.getId());
            destination.setImage(source.getImage());
            destination.setTitles(getTitleTranslations(source.getTranslations()));
            destination.setSubCategories(source.getSubCategories().stream().map(this::convertToDTO).toList());
    }

    private Map<String, String> getTitleTranslations(List<PlaceCategoryTranslation> translations){
        return translations.stream()
                .collect(Collectors.toMap(PlaceCategoryTranslation::getLocale, PlaceCategoryTranslation::getTitle));
    }


    public PlaceSubCategoryDTO convertToDTO(PlaceSubCategory placeSubCategory){
        return this.placeSubCategoriesMapper.toDto(placeSubCategory);
    }

}
