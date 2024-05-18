package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.services.api.PlaceSubCategoryApiService;
import tm.ugur.errors.placeSubCategories.PlaceSubCategoryErrorResponse;
import tm.ugur.errors.placeSubCategories.PlaceSubCategoryNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/place-sub-categories")
public class PlaceSubCategoryApiController {

    private final PlaceSubCategoryApiService placeSubCategoryService;


    @Autowired
    public PlaceSubCategoryApiController(PlaceSubCategoryApiService placeSubCategoryService) {
        this.placeSubCategoryService = placeSubCategoryService;
    }


    @GetMapping
    public ResponseEntity<List<PlaceSubCategoryDTO>> getPlaceSubCategories(){
        return ResponseEntity.ok(this.placeSubCategoryService.fetchPlaceSubCategories());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PlaceSubCategoryDTO> getPlace(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.placeSubCategoryService.fetchPlaceSubCategory(id).orElseThrow(PlaceSubCategoryNotFoundException::new));
    }


    @ExceptionHandler
    private ResponseEntity<PlaceSubCategoryErrorResponse> handleException(PlaceSubCategoryNotFoundException e){
        PlaceSubCategoryErrorResponse errorResponse = new PlaceSubCategoryErrorResponse(
                "Place sub category with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
