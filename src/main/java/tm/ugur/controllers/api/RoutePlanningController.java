package tm.ugur.controllers.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.geo.LineStringDTO;
import tm.ugur.services.api.RoutePlanningService;

import java.util.Map;

@RestController
@RequestMapping("/api/route-planning")
public class RoutePlanningController {

    private final RoutePlanningService routePlanningService;

    @Autowired
    public RoutePlanningController(RoutePlanningService routePlanningService) {
        this.routePlanningService = routePlanningService;
    }

    @GetMapping
    public ResponseEntity<Map<Integer, Map<String, LineStringDTO>>> planning(@RequestParam("pointALat") double pointALat,
                                                                @RequestParam("pointALng") double pointALng,
                                                                @RequestParam("pointBLat") double pointBLat,
                                                                @RequestParam("pointBLng") double pointBLng
    ) {

        return ResponseEntity.ok(routePlanningService.planning(pointALat, pointALng, pointBLat, pointBLng));
    }
}
