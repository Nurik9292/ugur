package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.PlaceCategory;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.util.pagination.PaginationService;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/place-categories")
public class PlaceCategoryController {

    private final PlaceCategoryService placeCategoryService;
    private final PaginationService paginationService;
    private static String sortByStatic = "";

    public PlaceCategoryController(PlaceCategoryService placeCategoryService,
                                   PaginationService paginationService) {
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

        Page<PlaceCategory> placeCategories = this.placeCategoryService.getPlaceCategoryPages(page, items, sortByStatic);
        int totalPages = placeCategories.getTotalPages();

        Integer[] totalPage = this.paginationService.getTotalPage(totalPages, placeCategories.getNumber());

        if(placeCategories.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, placeCategories.getTotalPages()).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }


        model.addAttribute("title", "Категория заведении");
        model.addAttribute("page", "place-category-index");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("totalPage", totalPage);


        return "/layouts/place_categories/index";
    }


    @GetMapping("/create")
    public String create(@ModelAttribute("placeCategory") PlaceCategory placeCategory, Model model){
        sortByStatic = "";

        model.addAttribute("title", "Создание категорию заведения");
        model.addAttribute("page", "place-category-create");

        return "layouts/place_categories/create";
    }

    @PostMapping
    public String store(@ModelAttribute("placeCategory") @Valid PlaceCategory placeCategory, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("page", "place-category-create");
            model.addAttribute("title", "Создать категорию заведения");
            return "layouts/place_categories/create";
        }

        placeCategoryService.store(placeCategory);

        return "redirect:/place-categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){
        sortByStatic = "";
        model.addAttribute("title", "Изменить категорию заведения");
        model.addAttribute("page", "place-category-edit");
        model.addAttribute("placeCategory", this.placeCategoryService.findOne(id).orElse(new PlaceCategory()));

        return "layouts/place_categories/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") long id,
                         @ModelAttribute("placeCategory") @Valid PlaceCategory placeCategory, BindingResult result,
                         Model model){

        if(result.hasErrors()){
            model.addAttribute("page", "place-category-edit");
            model.addAttribute("title", "Изменить категорию заведения");
            return "layouts/place_categories/edit";
        }

        this.placeCategoryService.update(id, placeCategory);

        return "redirect:/place-categories";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id){
        this.placeCategoryService.delete(id);
        return "redirect:/place-categories";
    }

}
