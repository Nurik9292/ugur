package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.util.mappers.PlaceCategoryMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlaceCategoryApiService {

    private final PlaceCategoryRepository placeCategoryRepository;
    private final PlaceCategoryMapper placeCategoryMapper;

    @Autowired
    public PlaceCategoryApiService(PlaceCategoryRepository placeCategoryRepository,
                                   PlaceCategoryMapper placeCategoryMapper) {
        this.placeCategoryRepository = placeCategoryRepository;
        this.placeCategoryMapper = placeCategoryMapper;
    }

    public List<PlaceCategoryDTO> fetchPlaceCategories(){
        return placeCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public Optional<PlaceCategoryDTO> fetchPlaceCategory(long id){
        return placeCategoryRepository.findById(id).map(this::convertToDto);
    }


    private PlaceCategoryDTO convertToDto(PlaceCategory placeCategory){
        return placeCategoryMapper.toDto(placeCategory);
    }
}
