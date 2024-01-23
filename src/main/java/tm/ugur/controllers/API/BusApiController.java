package tm.ugur.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Bus;
import tm.ugur.services.api.BusApiService;
import tm.ugur.util.errors.buses.BusErrorResponse;
import tm.ugur.util.errors.buses.BusNotFoundException;
import tm.ugur.util.errors.stop.StopErrorResponse;
import tm.ugur.util.errors.stop.StopNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusApiController {

    private final BusApiService busSservice;

    @Autowired
    public BusApiController(BusApiService busSservice) {
        this.busSservice = busSservice;
    }

    @GetMapping
    public List<BusDTO> getBuses(){
        return this.busSservice.getBuses();
    }

    @PostMapping("/{id}")
    public ResponseEntity<BusDTO> getBus(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.busSservice.getBus(id));
    }

    @ExceptionHandler
    private ResponseEntity<BusErrorResponse> handleException(BusNotFoundException e){
        BusErrorResponse errorResponse =
                new BusErrorResponse("Stop with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
