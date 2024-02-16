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

    private final BusApiService busSservice;
    private final StopService stopService;

    @Autowired
    public BusApiController(BusApiService busSservice, StopService stopService) {
        this.busSservice = busSservice;
        this.stopService = stopService;
    }

    @GetMapping
    public List<BusDTO> getBuses(){
        return this.busSservice.getBuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getBus(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.busSservice.getBus(id));
    }

    @ExceptionHandler
    private ResponseEntity<BusErrorResponse> handleException(BusNotFoundException e){
        BusErrorResponse errorResponse =
                new BusErrorResponse("There is no such bus!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
