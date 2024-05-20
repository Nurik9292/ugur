package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.api.BusApiService;
import tm.ugur.errors.buses.BusErrorResponse;
import tm.ugur.errors.buses.BusNotFoundException;
import tm.ugur.services.redis.RedisBusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/buses")
public class BusApiController {

    private final BusApiService busService;
    private final RedisBusService redisBusService;

    @Autowired
    public BusApiController(BusApiService busService, RedisBusService redisBusService) {
        this.busService = busService;
        this.redisBusService = redisBusService;
    }

    @GetMapping("/info")
    public List<BusDTO> getBuses(){
        return this.busService.getBuses();
    }

    @GetMapping ResponseEntity<Map<String, Integer>> getOnlineBus() {
        List<BusDTO> buses = redisBusService.getAll();
        Map<String, Integer> infoBus = new HashMap<>();
        infoBus.put("countBus", buses.size());
        infoBus.put("onlineBus",  buses.stream().filter(BusDTO::isStatus).toList().size());

        return ResponseEntity.ok().body(infoBus);
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
