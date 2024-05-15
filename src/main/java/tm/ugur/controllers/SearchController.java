package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceTranslation;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.util.mappers.PlaceMapper;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/search")
public class SearchController {

    private final PlaceService placeService;
    private final PlaceMapper placeMapper;


    @Autowired
    public SearchController(PlaceService placeService, PlaceMapper placeMapper) {
        this.placeService = placeService;
        this.placeMapper = placeMapper;
    }

    @GetMapping("/places")
    public ResponseEntity<?> placeSearch(@RequestParam(name = "search", required = false) String search){
        System.out.println(search);

        List<Place> placeList = placeService.findAll();
        List<Place> places = new ArrayList<>();

        for(Place place : placeList) {

           for(PlaceTranslation translation : place.getTranslations()) {
               if(translation.getLocale().equals("ru")) {
                   String  title = translation.getTitle().toLowerCase();
                    if(title.contains(search.toLowerCase()))
                        places.add(place);
               }
           }

        }

        List<PlaceDTO> finnish = places.stream().map(placeService::convertToDTO).toList();

        if (finnish.isEmpty()) {

            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(finnish);
        }
    }
}

