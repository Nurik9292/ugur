package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceCategory;
import tm.ugur.repo.PlaceRepository;
import tm.ugur.util.mappers.PlaceMapper;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceApiService {


    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceApiService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    public List<PlaceDTO> fetchPlaces(){
        return placeRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public Optional<PlaceDTO> fetchPlace(long id){
        return placeRepository.findById(id).map(this::convertToDTO);
    }


    public PlaceDTO convertToDTO(Place place){
        return this.placeMapper.toDto(place);
    }
}
