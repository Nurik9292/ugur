package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.util.mappers.PlaceMapper;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceApiService {


    private final PlaceRepository placeRepository;
    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceSubCategoryRepository placeSubCategoryRepository;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceApiService(PlaceRepository placeRepository,
                           PlaceCategoryRepository placeCategoryRepository,
                           PlaceSubCategoryRepository placeSubCategoryRepository,
                           PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeCategoryRepository = placeCategoryRepository;
        this.placeSubCategoryRepository = placeSubCategoryRepository;
        this.placeMapper = placeMapper;
    }

    public List<PlaceDTO> fetchPlaces(){
        return placeRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public Optional<PlaceDTO> fetchPlace(long id){
        return placeRepository.findById(id).map(this::convertToDTO);
    }

    public List<PlaceDTO> fetchPlacesForSubCategory(long id){
        Optional<PlaceSubCategory> placeSubCategory = placeSubCategoryRepository.findById(id);
        return placeRepository.findByPlaceSubCategory(placeSubCategory.orElseThrow()).stream().map(this::convertToDTO).toList();
    }

    public List<PlaceDTO> fetchPlacesForCategory(long id){
        Optional<PlaceCategory> placeCategory = placeCategoryRepository.findById(id);
        return placeRepository.findByPlaceCategory(placeCategory.orElseThrow()).stream().map(this::convertToDTO).toList();
    }

    public PlaceDTO convertToDTO(Place place){
        return this.placeMapper.toDto(place);
    }
}
