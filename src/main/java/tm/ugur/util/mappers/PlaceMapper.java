package tm.ugur.util.mappers;


import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.GeometryFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.*;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.*;

@Component
public class PlaceMapper extends AbstractMapper<Place, PlaceDTO> {


    private final ModelMapper modelMapper;
    private final PlaceCategoryMapper placeCategoryMapper;
    private final SocialNetworkMapper socialNetworkMapper;
    private final PlacePhoneMapper placePhoneMapper;
    private final PlaceSubCategoriesMapper placeSubCategoriesMapper;

    @Autowired
    public PlaceMapper(ModelMapper modelMapper,
                       PlaceCategoryMapper placeCategoryMapper,
                       SocialNetworkMapper socialNetworkMapper,
                       PlacePhoneMapper placePhoneMapper,
                       PlaceSubCategoriesMapper placeSubCategoriesMapper) {
        super(Place.class, PlaceDTO.class);
        this.modelMapper = modelMapper;
        this.placeCategoryMapper = placeCategoryMapper;
        this.socialNetworkMapper = socialNetworkMapper;
        this.placePhoneMapper = placePhoneMapper;
        this.placeSubCategoriesMapper = placeSubCategoriesMapper;
    }


    @PostConstruct
    public void setupMapper() {
        this.modelMapper.createTypeMap(Place.class, PlaceDTO.class)
                .addMappings(m -> m.skip(PlaceDTO::setLocation)).setPostConverter(toDtoConverter());
        this.modelMapper.createTypeMap(PlaceDTO.class, Place.class)
                .addMappings(m -> m.skip(Place::setLocation)).setPostConverter(toEntityConverter());
    }


    @Override
    public void mapSpecificFields(Place source, PlaceDTO destination) {
            destination.setId(source.getId());
            destination.setPhones(source.getPhones().stream().map(this::convertToDTO).toList());
            destination.setSocialNetworks(source.getSocialNetworks().stream().map(this::convertToDTO).toList());
            destination.setLocation(new PointDTO(source.getLocation().getX(), source.getLocation().getY()));
            destination.setPlaceCategory(convertToDTO(source.getPlaceCategory()));
            destination.setPlaceSubCategory(convertToDTO(source.getPlaceSubCategory()));
    }


    public PlaceCategoryDTO convertToDTO(PlaceCategory placeCategory){
        return placeCategoryMapper.toDto(placeCategory);
    }

    public SocialNetworkDTO convertToDTO(SocialNetwork socialNetwork){
        return socialNetworkMapper.toDto(socialNetwork);
    }

    public PlacePhoneDTO convertToDTO(PlacePhone placePhone){
        return placePhoneMapper.toDto(placePhone);
    }

    public PlaceSubCategoryDTO convertToDTO(PlaceSubCategory placeSubCategory){
        return placeSubCategoriesMapper.toDto(placeSubCategory);
    }

}
