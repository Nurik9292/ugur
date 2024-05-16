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
import org.springframework.web.multipart.MultipartFile;
import tm.ugur.models.*;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.util.pagination.PaginationService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/places")
public class PlaceController {

    @Value("${upload.image}")
    String uploadPth;

    private final PlaceService placeService;
    private final PlaceCategoryService placeCategoryService;
    private final PaginationService paginationService;



    private static String sortByStatic = "";
    private static String categoryId = "";

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
                        @RequestParam(value = "sortBy", required = false) String sortBy,
                        @RequestParam(name = "categoryId", required = false) String categoryId, Model model){

        if(sortBy != null){
            sortByStatic = sortBy;
        }

        Page<Place> places = this.placeService.getPlacePages(page, items, categoryId, sortByStatic);
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
        model.addAttribute("categoryId", categoryId == null ? 0 : Long.parseLong(categoryId));
        model.addAttribute("categories", placeCategoryService.findAll());


        return "layouts/places/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("place") Place place, Model model){
        sortByStatic = "";

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        model.addAttribute("title", "Создать заведение");
        model.addAttribute("page", "place-main-create");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("categoryTitles", placeCategoryService.getCategoryTitles(placeCategoryService.findAll()));

        return "layouts/places/create";
    }

    @PostMapping
    public ResponseEntity<?> store(
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "prev", required = false) MultipartFile prev,
            @ModelAttribute("place") @Valid Place place, BindingResult result){

        if(result.hasErrors()){;
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        placeService.store(place, files, prev);

        return ResponseEntity.ok("Заведение успешно добавленно");
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){
        sortByStatic = "";

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
                         @RequestParam(value = "files", required = false) MultipartFile[] files,
                         @RequestParam(value = "prev", required = false) MultipartFile prev,
                         @RequestParam(value = "removeImageIds", required = false) long[] removedImageIds,
                         @ModelAttribute("place") @Valid Place place, BindingResult result){

        System.out.println(prev);

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        System.out.println(Arrays.toString(removedImageIds));

        this.placeService.update(id, place, files, prev, removedImageIds);

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