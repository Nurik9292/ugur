package tm.ugur.controllers.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.dto.RouteDTO;
import tm.ugur.services.api.RouteApiService;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteApiController {

    private final RouteApiService routeService;

    @Autowired
    public RouteApiController(RouteApiService routeService) {
        this.routeService = routeService;
    }



    @GetMapping
    public List<RouteDTO> getRoutes(){
        return this.routeService.findAll();
    }

    @GetMapping("/{id}")
    public RouteDTO getRoute(@PathVariable("id") int id){
        return this.routeService.findOne(id);
    }
}
