package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.StopDTO;
import tm.ugur.services.api.StopApiService;
import tm.ugur.errors.stop.StopErrorResponse;
import tm.ugur.errors.stop.StopNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopApiController {

    private final StopApiService stopService;

    @Autowired
    public StopApiController(StopApiService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public ResponseEntity<List<StopDTO>>  getStops(){
        return ResponseEntity.ok(this.stopService.getStops());
    }


    @GetMapping("/{id}")
    public StopDTO getStop(@PathVariable("id") Long id){
        return this.stopService.getStop(id);
    }


    @ExceptionHandler
    private ResponseEntity<StopErrorResponse> handleException(StopNotFoundException e){
        StopErrorResponse errorResponse = new StopErrorResponse(
                "Stop with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
