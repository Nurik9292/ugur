package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.models.*;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceSubCategoryService;
import tm.ugur.util.pagination.PaginationService;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceCategoryService placeCategoryService;
    private final PlaceSubCategoryService placeSubCategoryService;
    private final PaginationService paginationService;

    private static String sortByStatic = "";

    @Autowired
    public PlaceController(PlaceService placeService,
                           PlaceCategoryService placeCategoryService,
                           PlaceSubCategoryService placeSubCategoryService,
                           PaginationService paginationService) {
        this.placeService = placeService;
        this.placeCategoryService = placeCategoryService;
        this.placeSubCategoryService = placeSubCategoryService;
        this.paginationService = paginationService;

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
        model.addAttribute("page", "place-main-index");
        model.addAttribute("places", places);
        model.addAttribute("totalPage", totalPage);


        return "layouts/places/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("place") Place place, Model model){
        sortByStatic = "";

        model.addAttribute("title", "Заведение");
        model.addAttribute("page", "place-main-create");
        model.addAttribute("placeCategories", placeCategoryService.findAll());
        model.addAttribute("placeSubCategory", placeSubCategoryService.findAll());

        return "layouts/places/create";
    }

    @PostMapping
    public String store(@RequestParam(value = "social_networks", required = false) List<String> socialNetworks,
                        @RequestParam(value = "phones", required = false) List<String> phones,
                        @RequestParam(value = "image", required = false) MultipartFile image,
                        @ModelAttribute("place") @Valid Place place, BindingResult result, Model model){


        placeService.store(place, socialNetworks, phones, image);

        return "redirect:/places";
    }

    @CrossOrigin(origins = "http://192.168.37.61:8083")
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){
        sortByStatic = "";

        Place place = placeService.findOne(id).orElse(new Place());
        List<SocialNetwork> socialNetworks = place.getSocialNetworks();

        if(!socialNetworks.isEmpty() && socialNetworks.getFirst().getName().equalsIgnoreCase("instagram")){
            model.addAttribute("instagram", socialNetworks.getFirst().getName());
        }
        if(!socialNetworks.isEmpty() && socialNetworks.getLast().getName().equalsIgnoreCase("tiktok")){
            model.addAttribute("tiktok", socialNetworks.getLast().getName());
        }

        List<PlacePhone> phones = place.getPhones();

        model.addAttribute("cityNumber",
                phones.stream().filter(phone -> phone.getType().equalsIgnoreCase("city"))
                        .findAny().orElse(new PlacePhone()).getNumber());

        model.addAttribute("mobNumbers", phones.stream().filter(phone ->
                phone.getType().equalsIgnoreCase("mob")).map(PlacePhone::getNumber).toList());

        model.addAttribute("title", "Изменить заведение");
        model.addAttribute("page", "place-main-edit");
        model.addAttribute("placeCategories", placeCategoryService.findAll());
        model.addAttribute("place", place);

        return "layouts/places/edit";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") long id,
                         @RequestParam(value = "social_networks", required = false) List<String> socialNetworks,
                         @RequestParam(value = "phones", required = false) List<String> phones,
                         @RequestParam(value = "image", required = false) MultipartFile image,
                         @ModelAttribute("place") @Valid Place place, BindingResult result,
                         Model model){

        if(result.hasErrors()){
            model.addAttribute("page", "place-main-create");
            model.addAttribute("title", "Обновить заведение");
            return "layouts/stops/edit";
        }


        this.placeService.update(id, place, socialNetworks, phones, image);

        return "redirect:/places";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id){
        this.placeService.delete(id);
        return "redirect:/places";
    }
}