package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.repo.PlaceCategoryRepository;
import tm.ugur.repo.PlaceSubCategoryRepository;
import tm.ugur.util.mappers.PlaceCategoryMapper;
import tm.ugur.util.mappers.PlaceSubCategoriesMapper;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceSubCategoryApiService {

    private final PlaceSubCategoryRepository placeSubCategoryRepository;
    private final PlaceSubCategoriesMapper placeSubCategoryMapper;

    @Autowired
    public PlaceSubCategoryApiService(PlaceSubCategoryRepository placeSubCategoryRepository,
                                   PlaceSubCategoriesMapper placeSubCategoryMapper) {
        this.placeSubCategoryRepository = placeSubCategoryRepository;
        this.placeSubCategoryMapper = placeSubCategoryMapper;
    }

    public List<PlaceSubCategoryDTO> fetchPlaceSubCategories(){
        return placeSubCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public Optional<PlaceSubCategoryDTO> fetchPlaceSubCategory(long id){
        return placeSubCategoryRepository.findById(id).map(this::convertToDto);
    }


    private PlaceSubCategoryDTO convertToDto(PlaceSubCategory placeSubCategory){
        return placeSubCategoryMapper.toDto(placeSubCategory);
    }
}
