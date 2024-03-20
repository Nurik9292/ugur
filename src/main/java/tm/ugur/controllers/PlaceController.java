package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceCategoryService placeCategoryService;
    private final PaginationService paginationService;

    private static String sortByStatic = "";

    @Autowired
    public PlaceController(PlaceService placeService,
                           PlaceCategoryService placeCategoryService,
                           PaginationService paginationService) {
        this.placeService = placeService;
        this.placeCategoryService = placeCategoryService;
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

        model.addAttribute("title", "Создать заведение");
        model.addAttribute("page", "place-main-create");
        model.addAttribute("placeCategories", placeCategoryService.findAll());

        return "layouts/places/create";
    }

    @PostMapping
    public ResponseEntity<?> store(
            @RequestParam(value = "instagram", required = false) String instagram,
            @RequestParam(value = "tiktok", required = false) String tiktok,
                        @RequestParam(value = "telephones", required = false) List<String> telephones,
                        @RequestParam(value = "cityPhone", required = false) String cityPhone,
                        @RequestParam(value = "file", required = false) MultipartFile file,
                        @ModelAttribute("place") @Valid Place place, BindingResult result){


        if(result.hasErrors()){;
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        System.out.println(telephones);

        placeService.store(place, instagram, tiktok, telephones, cityPhone, file);

        return ResponseEntity.ok("Заведение успешно добавленно");
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){
        sortByStatic = "";

        Place place = placeService.findOne(id).orElse(new Place());
        List<SocialNetwork> socialNetworks = new ArrayList<>(place.getSocialNetworks());

        if(!socialNetworks.isEmpty() && socialNetworks.  getFirst().getName().equalsIgnoreCase("instagram")){
            model.addAttribute("instagram", socialNetworks.getFirst().getName());
        }
        if(!socialNetworks.isEmpty() && socialNetworks.getLast().getName().equalsIgnoreCase("tiktok")){
            model.addAttribute("tiktok", socialNetworks.getLast().getName());
        }

        Set<PlacePhone> phones = place.getPhones();

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
    public ResponseEntity<?> update(@PathVariable("id") long id,
                         @RequestParam(value = "instagram", required = false) String instagram,
                         @RequestParam(value = "tiktok", required = false) String tiktok,
                         @RequestParam(value = "telephones", required = false) List<String> telephones,
                         @RequestParam(value = "cityPhone", required = false) String cityPhone,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @ModelAttribute("place") @Valid Place place, BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        System.out.println(telephones);

        this.placeService.update(id, place, instagram, tiktok, telephones, cityPhone, file);

        return ResponseEntity.ok("Заведение успешно измененно");
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id){
        this.placeService.delete(id);
        return "redirect:/places";
    }

}