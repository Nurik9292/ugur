package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceCategoryTranslation;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceSubCategoryService;
import tm.ugur.util.pagination.PaginationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/place-sub-categories")
public class PlaceSubCategoryController {

    private final PlaceSubCategoryService placeSubCategoryService;
    private final PlaceCategoryService placeCategoryService;
    private final PaginationService paginationService;
    private static String sortByStatic = "";

    public PlaceSubCategoryController(PlaceSubCategoryService placeSubCategoryService,
                                      PlaceCategoryService placeCategoryService,
                                      PaginationService paginationService) {
        this.placeSubCategoryService = placeSubCategoryService;
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

        Page<PlaceSubCategory> placeSubCategories = this.placeSubCategoryService.getPlaceSubCategoryPages(page, items, sortByStatic);
        int totalPages = placeSubCategories.getTotalPages();

        Integer[] totalPage = this.paginationService.getTotalPage(totalPages, placeSubCategories.getNumber());

        if(placeSubCategories.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, placeSubCategories.getTotalPages()).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }


        model.addAttribute("title", "Под категория заведении");
        model.addAttribute("page", "place-sub-category-index");
        model.addAttribute("placeSubCategories", placeSubCategories);
        model.addAttribute("totalPage", totalPage);


        return "layouts/place_sub_categories/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("placeSubCategory") PlaceSubCategory placeSubCategory, Model model){
        sortByStatic = "";

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();


        model.addAttribute("title", "Создание под категорию заведения");
        model.addAttribute("page", "place-sub-category-create");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("titles", placeCategoryService.getCategoryTitles(placeCategories));

        return "layouts/place_sub_categories/create";
    }

    @PostMapping
    public String store(@RequestParam("title_tm") String title_tm,
                        @RequestParam("title_ru") String title_ru,
                        @RequestParam("title_en") String title_en,
                        @ModelAttribute("placeSubCategory") @Valid PlaceSubCategory placeSubCategory,
                        BindingResult result, Model model){

        if(result.hasErrors()){
            model.addAttribute("page", "place-sub-category-create");
            model.addAttribute("title", "Создать под категорию заведения");
            return "layouts/place_sub_categories/create";
        }

        placeSubCategoryService.store(placeSubCategory, title_tm, title_ru,title_en);

        return "redirect:/place-sub-categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){
        sortByStatic = "";

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        PlaceSubCategory placeSubCategory = placeSubCategoryService.findOne(id).orElse(new PlaceSubCategory());
        List<PlaceSubCategoryTranslation> translations = placeSubCategory.getTranslations();
        Map<String, String> translation = new HashMap<>();

        for (PlaceSubCategoryTranslation pct : translations) {
            translation.put(pct.getLocale(), pct.getTitle());
        }

        model.addAttribute("title", "Изменить под категорию заведения");
        model.addAttribute("page", "place-sub-category-edit");
        model.addAttribute("placeSubCategory", placeSubCategory);
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("titles", placeCategoryService.getCategoryTitles(placeCategories));
        model.addAttribute("translation", translation);

        return "layouts/place_sub_categories/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") long id,
                         @RequestParam("title_tm") String title_tm,
                         @RequestParam("title_ru") String title_ru,
                         @RequestParam("title_en") String title_en,
                         @ModelAttribute("placeSubCategory") @Valid PlaceSubCategory placeSubCategory, BindingResult result,
                         Model model){

        if(result.hasErrors()){
            model.addAttribute("page", "place-sub-category-edit");
            model.addAttribute("title", "Изменить под категорию заведения");
            return "layouts/place_sub_categories/edit";
        }

        this.placeSubCategoryService.update(id, placeSubCategory, title_tm, title_ru, title_en);

        return "redirect:/place-sub-categories";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id){
        this.placeSubCategoryService.delete(id);
        return "redirect:/place-sub-categories";
    }

}
