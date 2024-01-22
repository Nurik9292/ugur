package tm.ugur.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.api.BusApiService;

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
}
