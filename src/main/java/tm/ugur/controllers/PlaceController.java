package tm.ugur.controllers;

import io.netty.handler.codec.http.multipart.FileUpload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.models.Place;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.storage.FileSystemStorageService;
import tm.ugur.util.pagination.PaginationService;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PaginationService paginationService;
    private final FileSystemStorageService storageService;

    private static String sortByStatic = "";

    @Autowired
    public PlaceController(PlaceService placeService,
                           PaginationService paginationService,
                           FileSystemStorageService storageService) {
        this.placeService = placeService;
        this.paginationService = paginationService;
        this.storageService = storageService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        if(sortBy != null){
            sortByStatic = sortBy;
        }

        Page<Place> places = this.placeService.getPlacePages(page, items, sortByStatic);
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

    @GetMapping("/create")
    public String create(@ModelAttribute("place") Place place, Model model){
        sortByStatic = "";

        model.addAttribute("title", "Заведение");
        model.addAttribute("page", "place-create");

        return "layouts/places/create";
    }

    @PostMapping
    public String store(@RequestParam(value = "social_networks", required = false) List<String> socialNetworks,
                        @RequestParam(value = "phones", required = false) List<String> phones,
                        @RequestParam("lat") double lat,
                        @RequestParam("lng") double lng,
                        @RequestParam(value = "image", required = false) MultipartFile image,
                        @ModelAttribute("place") @Valid Place place, BindingResult result, Model model){

        String pathImage = storageService.store(image);
        placeService.store(place, socialNetworks, phones, pathImage, lat, lng);

        return "redirect:/places";
    }
}