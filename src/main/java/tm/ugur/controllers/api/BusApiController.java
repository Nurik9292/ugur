package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.api.BusApiService;
import tm.ugur.util.errors.buses.BusErrorResponse;
import tm.ugur.util.errors.buses.BusNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/buses")
public class BusApiController {

    private final BusApiService busService;
    private final StopService stopService;

    @Autowired
    public BusApiController(BusApiService busService, StopService stopService) {
        this.busService = busService;
        this.stopService = stopService;
    }

    @GetMapping
    public List<BusDTO> getBuses(){
        return this.busService.getBuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBus(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.busService.getBus(id));
    }

    @GetMapping("/next-stop/{id}")
    public ResponseEntity<String> nextStop(@PathVariable("id") Long id,
                                           @RequestParam("car_number") String carNumber){

        return ResponseEntity.ok(busService.getNextStop(id, carNumber));
    }

    @ExceptionHandler
    private ResponseEntity<BusErrorResponse> handleException(BusNotFoundException e){
        BusErrorResponse errorResponse =
                new BusErrorResponse("There is no such bus!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
