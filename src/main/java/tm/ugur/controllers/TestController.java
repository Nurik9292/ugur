package tm.ugur.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tm.ugur.models.*;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceSubCategoryTranslationService;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.api.ApiService;
import tm.ugur.services.parser.ParserService;

import java.util.*;
import java.util.List;

@Controller
public class TestController {

    private final ParserService parserService;
    private final PlaceService placeService;
    private final PlaceSubCategoryTranslationService translationService;
    private final ApiService apiService;
    private final RouteService routeService;

    @Autowired
    public TestController(ParserService parserService,
                          PlaceService placeService,
                          PlaceSubCategoryTranslationService translationService, ApiService apiService, RouteService routeService) {
        this.parserService = parserService;
        this.placeService = placeService;
        this.translationService = translationService;
        this.apiService = apiService;
        this.routeService = routeService;
    }

    @GetMapping("/test")
    public String test(){

        Route route = routeService.findByNumber(28).get();

        List<StartRouteStop> routeStops = route.getStartRouteStops();



        List<Stop> stops = routeStops.stream()
                .sorted(Comparator.comparing(StartRouteStop::getIndex))
                .map(rs -> rs.getStop()).toList();

        System.out.println(stops);

        return "null";
    }
}
