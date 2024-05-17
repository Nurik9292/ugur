package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.models.Place;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceTranslation;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.util.mappers.PlaceMapper;
import tm.ugur.util.pagination.PaginationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/search")
public class SearchController {

    private final PlaceService placeService;
    private final  PaginationUtil paginationUtil;
    private final PlaceCategoryService placeCategoryService;

    @Autowired
    public SearchController(PlaceService placeService,
                            PaginationUtil paginationUtil,
                            PlaceCategoryService placeCategoryService) {
        this.placeService = placeService;
        this.paginationUtil = paginationUtil;
        this.placeCategoryService = placeCategoryService;
    }

    @GetMapping("/places")
    public String placeSearch(@RequestParam(name = "search", required = false) String search, Model model){

        List<Place> placeList = placeService.search(search, "ru");
        Page<Place> places = paginationUtil.createPage(placeList, 1, 25);

        model.addAttribute("title", "Заведение");
        model.addAttribute("page", "place-main-index");
        model.addAttribute("places", places);
        model.addAttribute("category", 0);
        model.addAttribute("categories", placeCategoryService.findAll());

        return "layouts/places/index";
    }
}

