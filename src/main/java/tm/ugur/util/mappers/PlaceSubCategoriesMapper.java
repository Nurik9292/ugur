package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.models.PlaceSubCategory;

@Component
public class PlaceSubCategoriesMapper extends AbstractMapper<PlaceSubCategory, PlaceSubCategoryDTO>{


    private final ModelMapper modelMapper;
    @Autowired
    public PlaceSubCategoriesMapper(ModelMapper modelMapper) {
        super(PlaceSubCategory.class, PlaceSubCategoryDTO.class);
        this.modelMapper = modelMapper;
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
    }
}
