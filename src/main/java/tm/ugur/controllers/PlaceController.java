package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tm.ugur.models.Place;
import tm.ugur.models.Route;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.util.pagination.PaginationService;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PaginationService paginationService;

    private static String sortByStatic = "";

    @Autowired
    public PlaceController(PlaceService placeService, PaginationService paginationService) {
        this.placeService = placeService;
        this.paginationService = paginationService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        if(sortBy != null){
            sortByStatic = sortBy;
        }

        Page<Place> places = this.placeService.getRoutePages(page, items, sortByStatic);
        int totalPages = places.getTotalPages();
        Integer[] totalPage = this.paginationService.getTotalPage(totalPages, places.getNumber());

        if(places.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, places.getTotalPages()).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("title", "Заведение");
        model.addAttribute("page", "place-index");
        model.addAttribute("places", places);
        model.addAttribute("totalPage", totalPage);

        return "layouts/places/index";
    }
}
