package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.models.place.subCategory.PlaceSubCategoryTranslation;
import tm.ugur.request.place.SubCategoryRequest;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.services.admin.PlaceSubCategoryService;
import tm.ugur.util.pagination.PaginationUtil;
import tm.ugur.util.sort.place.SortSubCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/place-sub-categories")
public class PlaceSubCategoryController {

    private final PlaceSubCategoryService placeSubCategoryService;
    private final PlaceCategoryService placeCategoryService;
    private final PaginationUtil paginationUtil;
    private final SortSubCategory sortSubCategory;

    @Autowired
    public PlaceSubCategoryController(PlaceSubCategoryService placeSubCategoryService,
                                      PlaceCategoryService placeCategoryService,
                                      PaginationUtil paginationUtil, SortSubCategory sortSubCategory) {
        this.placeSubCategoryService = placeSubCategoryService;
        this.placeCategoryService = placeCategoryService;
        this.paginationUtil = paginationUtil;
        this.sortSubCategory = sortSubCategory;
    }


    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        List<PlaceSubCategory> subCategories = placeSubCategoryService.findAll();
        sortSubCategory.setData(subCategories, sortBy);
        sortSubCategory.execute();
        Page<PlaceSubCategory> placeSubCategories = paginationUtil.createPage(subCategories,pageNumber, itemsPerPage);
        int totalPages = placeSubCategories.getTotalPages();

        if(placeSubCategories.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, placeSubCategories.getTotalPages()).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("title", "Под категория заведении");
        model.addAttribute("page", "place-sub-category-index");
        model.addAttribute("sort", sortBy);
        model.addAttribute("placeSubCategories", placeSubCategories);
        model.addAttribute("totalPage", paginationUtil.getTotalPage(totalPages, placeSubCategories.getNumber()));


        return "layouts/place_sub_categories/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("placeSubCategory") PlaceSubCategory placeSubCategory, Model model){

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        model.addAttribute("title", "Создание под категорию заведения");
        model.addAttribute("page", "place-sub-category-create");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("titles", placeCategoryService.getCategoryTitles(placeCategories));

        return "layouts/place_sub_categories/create";
    }

    @PostMapping
    public String store(@ModelAttribute @Valid SubCategoryRequest request, BindingResult result, Model model){

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            model.addAttribute("errors", errors);
            model.addAttribute("page", "place-sub-category-create");
            model.addAttribute("title", "Создать под категорию заведения");
            model.addAttribute("placeCategories", placeCategories);
            model.addAttribute("titles", placeCategoryService.getCategoryTitles(placeCategories));
            return "layouts/place_sub_categories/create";
        }

        placeSubCategoryService.store(request);

        return "redirect:/place-sub-categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){

        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        PlaceSubCategory placeSubCategory = placeSubCategoryService.findOne(id);
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
                         @ModelAttribute @Valid SubCategoryRequest request, BindingResult result,
                         Model model){
        List<PlaceCategory> placeCategories = placeCategoryService.findAll();

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            model.addAttribute("errors", errors);
            model.addAttribute("page", "place-sub-category-edit");
            model.addAttribute("title", "Изменить под категорию заведения");
            model.addAttribute("placeCategories", placeCategories);
            model.addAttribute("titles", placeCategoryService.getCategoryTitles(placeCategories));
            return "layouts/place_sub_categories/edit";
        }

        this.placeSubCategoryService.update(id, request);

        return "redirect:/place-sub-categories";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id){
        this.placeSubCategoryService.delete(id);
        return "redirect:/place-sub-categories";
    }

}
