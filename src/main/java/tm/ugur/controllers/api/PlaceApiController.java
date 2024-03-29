package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.models.Place;
import tm.ugur.services.api.PlaceApiService;
import tm.ugur.util.errors.places.PlaceErrorResponse;
import tm.ugur.util.errors.places.PlaceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceApiController {

    private final PlaceApiService placeApiService;

    @Autowired
    public PlaceApiController(PlaceApiService placeApiService) {
        this.placeApiService = placeApiService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces(){
        return ResponseEntity.ok(this.placeApiService.fetchPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlace(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.placeApiService.fetchPlace(id).orElseThrow(PlaceNotFoundException::new));
    }

    @GetMapping("/sub-categories/{id}")
    public ResponseEntity<List<PlaceDTO>> getPlaceForSubCategory(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.placeApiService.fetchPlacesForSubCategory(id));
    }

    @GetMapping("categories/{id}")
    public ResponseEntity<List<PlaceDTO>> getPlaceForCategory(@PathVariable("id") Long id){
        List<PlaceDTO> places = placeApiService.fetchPlacesForCategory(id);
        return ResponseEntity.ok(places);
    }


    @ExceptionHandler
    private ResponseEntity<PlaceErrorResponse> handleException(PlaceNotFoundException e){
        PlaceErrorResponse errorResponse = new PlaceErrorResponse(
                "Place with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
