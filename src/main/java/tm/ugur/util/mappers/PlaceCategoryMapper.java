package tm.ugur.util.mappers;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Bus;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;


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
            destination.setTitle(source.getTitle());
            destination.setSubCategories(source.getSubCategories().stream().map(this::convertToDTO).toList());
    }


    public PlaceSubCategoryDTO convertToDTO(PlaceSubCategory placeSubCategory){
        return this.placeSubCategoriesMapper.toDto(placeSubCategory);
    }

}
