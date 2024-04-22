package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.security.ClientDetails;
import tm.ugur.util.errors.places.PlaceNotFoundException;
import tm.ugur.util.mappers.PlaceMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
        List<PlaceDTO> places = placeRepository.findAll().stream().map(this::convertToDTO).toList();
        places.forEach(place -> place.setFavorite(isFavorite(place)));
        return places;
    }

    public Optional<PlaceDTO> fetchPlace(long id){
        return placeRepository.findById(id).map(this::convertToDTO);
    }

    public Optional<Place> getPlace(long id){
        return placeRepository.findById(id);
    }

    public List<PlaceDTO> fetchPlacesForSubCategory(long id){
        Optional<PlaceSubCategory> placeSubCategory = placeSubCategoryRepository.findById(id);
        return placeSubCategory.map(subCategory -> placeRepository.findByPlaceSubCategory(subCategory)
                .stream().map(this::convertToDTO).toList()).orElseThrow(PlaceNotFoundException::new);
    }

    public List<PlaceDTO> fetchPlacesForCategory(long id){
        Optional<PlaceCategory> placeCategory = placeCategoryRepository.findById(id);
        return placeCategory.map(category -> placeRepository.findByPlaceCategory(category)
                .stream().map(this::convertToDTO).toList()).orElseThrow(PlaceNotFoundException::new);
    }

    private boolean isFavorite(PlaceDTO placeDTO){
        Client client = getAuthClient();
        return client.getPlaces().stream().anyMatch(place ->  place.getId()  == placeDTO.getId());
    }


    @Transactional
    public void store(Place place){
        placeRepository.save(place);
    }

    public PlaceDTO convertToDTO(Place place){
        return this.placeMapper.toDto(place);
    }


    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }

}
