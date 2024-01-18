package tm.ugur.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.dto.StopDTO;
import tm.ugur.services.api.StopAPiService;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopApiController {

    private final StopAPiService stopService;

    @Autowired
    public StopApiController(StopAPiService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public List<StopDTO> getStops(){
        return this.stopService.findAll();
    }

    @GetMapping("/{id}")
    public StopDTO getStop(@PathVariable("id") int id){
        return this.stopService.findOne(id);
    }


}
