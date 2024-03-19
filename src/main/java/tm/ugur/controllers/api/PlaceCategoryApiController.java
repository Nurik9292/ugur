package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.PlaceCategoryDTO;
import tm.ugur.services.api.PlaceCategoryApiService;
import tm.ugur.util.errors.placeCategories.PlaceCategoryErrorResponse;
import tm.ugur.util.errors.placeCategories.PlaceCategoryNotFoundException;
import tm.ugur.util.errors.places.PlaceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/place-categories")
public class PlaceCategoryApiController {

    private final PlaceCategoryApiService placeCategoryService;


    @Autowired
    public PlaceCategoryApiController(PlaceCategoryApiService placeCategoryService) {
        this.placeCategoryService = placeCategoryService;
    }


    @GetMapping
    public ResponseEntity<List<PlaceCategoryDTO>> getPlaceCategories(){
        return ResponseEntity.ok(this.placeCategoryService.fetchPlaceCategories());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PlaceCategoryDTO> getPlace(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.placeCategoryService.fetchPlaceCategory(id).orElseThrow(PlaceNotFoundException::new));
    }


    @ExceptionHandler
    private ResponseEntity<PlaceCategoryErrorResponse> handleException(PlaceCategoryNotFoundException e){
        PlaceCategoryErrorResponse errorResponse = new PlaceCategoryErrorResponse(
                "Place category with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
