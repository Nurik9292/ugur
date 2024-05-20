package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.*;
import tm.ugur.models.place.*;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.request.PlaceRequest;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceTranslationService;
import tm.ugur.util.pagination.PaginationUtil;
import tm.ugur.util.sort.place.SortPlace;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Controller
@RequestMapping("/places")
public class PlaceController {

    @Value("${upload.image}")
    String uploadPth;

    private final PlaceService placeService;
    private final PlaceTranslationService placeTranslationService;
    private final PlaceCategoryService placeCategoryService;
    private final PaginationUtil paginationUtil;
    private final SortPlace sortPlace;


    @Autowired
    public PlaceController(PlaceService placeService,
                           PlaceTranslationService placeTranslationService,
                           PlaceCategoryService placeCategoryService,
                           PaginationUtil paginationUtil,
                           SortPlace sortPlace) {
        this.placeService = placeService;
        this.placeTranslationService = placeTranslationService;
        this.placeCategoryService = placeCategoryService;
        this.paginationUtil = paginationUtil;
        this.sortPlace = sortPlace;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy,
                        @RequestParam(name = "category", required = false) String category,
                        @RequestParam(name = "search", required = false) String search,
                        Model model){

        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);
        long categoryId = category != null && category.matches("^(?!\\s*$)\\d+$")  ? Long.parseLong(category) : 0;

        List<Place> placeList =  search == null ? placeService.findAll(categoryId) : placeService.search(search, "ru");
        sortPlace.setData(placeList, sortBy);
        sortPlace.execute();
        Page<Place> places = paginationUtil.createPage(placeList, pageNumber, itemsPerPage);

        model.addAttribute("title", "Заведение");
        model.addAttribute("page", "place-main-index");
        model.addAttribute("places", places);
        model.addAttribute("sort", Objects.nonNull(sortBy) ? sortBy : "");
        model.addAttribute("totalPage", this.paginationUtil.getTotalPage(places.getTotalPages(), places.getNumber()));
        model.addAttribute("category", categoryId);
        model.addAttribute("search", search);
        model.addAttribute("categories", placeCategoryService.findAll());

        return "layouts/places/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("place") Place place, Model model){

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        model.addAttribute("title", "Создать заведение");
        model.addAttribute("page", "place-main-create");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("categoryTitles", placeCategoryService.getCategoryTitles(placeCategoryService.findAll()));

        return "layouts/places/create";
    }


    @PostMapping
    public ResponseEntity<?> store(@ModelAttribute @Valid PlaceRequest $request, BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        placeService.store($request);

        return ResponseEntity.ok("Заведение успешно добавленно");
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){

        Place place = placeService.findOne(id);
        List<SocialNetwork> socialNetworks = new ArrayList<>(place.getSocialNetworks());

        if(!socialNetworks.isEmpty() && socialNetworks.  getFirst().getName().equalsIgnoreCase("instagram")){
            model.addAttribute("instagram", socialNetworks.getFirst().getName());
        }
        if(!socialNetworks.isEmpty() && socialNetworks.getLast().getName().equalsIgnoreCase("tiktok")){
            model.addAttribute("tiktok", socialNetworks.getLast().getName());
        }

        Set<PlaceTranslation> translations = place.getTranslations();
        Map<String, String> titles = new HashMap<>();
        Map<String, String> address = new HashMap<>();

        for (PlaceTranslation t : translations) {
            titles.put(t.getLocale(), t.getTitle());
            address.put(t.getLocale(), t.getAddress());
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
        model.addAttribute("categoryTitles", placeCategoryService.getCategoryTitles(placeCategoryService.findAll()));
        model.addAttribute("titles", titles);
        model.addAttribute("address", address);
        PlaceThumb thumb = place.getThumbs();
        if(thumb != null)
            model.addAttribute("thumb", placeService.pruningPath(thumb.getPath()));

        return "layouts/places/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id,
                         @ModelAttribute("place") @Valid PlaceRequest request, BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        this.placeService.update(id, request);

        return ResponseEntity.ok("Заведение успешно измененно");
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id){
        this.placeService.delete(id);
        return "redirect:/places";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity< Map<Long, Map<String,Long>>>  getImages(@PathVariable("id") Long id){
        System.out.println(id);
        Place place = placeService.findOne(id);
        Map<Long, Map<String,Long>>  images = new HashMap<>();
        place.getImages().forEach(image -> {
                String imageName = placeService.pruningPath(image.getPath());
                images.put(image.getId(), new HashMap<>(Map.of(imageName, getImageSize(Paths.get(uploadPth + "/place/", imageName)))));
            });


        return ResponseEntity.ok().body(images);
    }

    private long getImageSize(Path path){
        try {
            return new PathResource(path).contentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}