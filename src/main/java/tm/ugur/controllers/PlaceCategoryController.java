package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.PlaceSubCategoryDTO;
import tm.ugur.models.place.category.PlaceCategory;
import tm.ugur.models.place.category.PlaceCategoryTranslation;
import tm.ugur.models.place.subCategory.PlaceSubCategory;
import tm.ugur.request.place.CategoryRequest;
import tm.ugur.services.admin.PlaceCategoryService;
import tm.ugur.util.mappers.PlaceSubCategoriesMapper;
import tm.ugur.util.pagination.PaginationUtil;
import tm.ugur.util.sort.place.SortCategory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/place-categories")
public class PlaceCategoryController {

    private final PlaceCategoryService placeCategoryService;
    private final PaginationUtil paginationUtil;
    private final SortCategory sortCategory;
    private final PlaceSubCategoriesMapper placeSubCategoriesMapper;


    public PlaceCategoryController(PlaceCategoryService placeCategoryService,
                                   PaginationUtil paginationUtil,
                                   SortCategory sortCategory,
                                   PlaceSubCategoriesMapper placeSubCategoriesMapper) {
        this.placeCategoryService = placeCategoryService;
        this.paginationUtil = paginationUtil;
        this.sortCategory = sortCategory;
        this.placeSubCategoriesMapper = placeSubCategoriesMapper;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);


        List<PlaceCategory> placeCategoryList =  placeCategoryService.findAll();
        sortCategory.setData(placeCategoryList, sortBy);
        sortCategory.execute();

        Page<PlaceCategory> placeCategories = paginationUtil.createPage(placeCategoryList, pageNumber, itemsPerPage);
        int totalPages = placeCategories.getTotalPages();

        if(placeCategories.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, placeCategories.getTotalPages()).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("title", "Категория заведении");
        model.addAttribute("page", "place-category-index");
        model.addAttribute("placeCategories", placeCategories);
        model.addAttribute("totalPage", paginationUtil.getTotalPage(totalPages, placeCategories.getNumber()));
        model.addAttribute("sort", Objects.nonNull(sortBy) ? sortBy : "");

        return "layouts/place_categories/index";
    }


    @GetMapping("/create")
    public String create(@ModelAttribute("placeCategory") PlaceCategory placeCategory, Model model){


        model.addAttribute("title", "Создание категорию заведения");
        model.addAttribute("page", "place-category-create");


        return "layouts/place_categories/create";
    }


        @PostMapping
    public String store(@ModelAttribute @Valid CategoryRequest request, BindingResult result, Model model){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            model.addAttribute("errors", errors);
            model.addAttribute("page", "place-category-create");
            model.addAttribute("title", "Создать категорию заведения");
            return "layouts/place_categories/create";
        }

        placeCategoryService.store(request);

        return "redirect:/place-categories";
    }





    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model){

        PlaceCategory placeCategory = placeCategoryService.findOne(id);

        System.out.println(placeCategory);

        List<PlaceCategoryTranslation> translations = placeCategory.getTranslations();
        Map<String, String> translation = new HashMap<>();

        for (PlaceCategoryTranslation pct : translations) {
            translation.put(pct.getLocale(), pct.getTitle());
        }

        model.addAttribute("title", "Изменить категорию заведения");
        model.addAttribute("page", "place-category-edit");
        model.addAttribute("placeCategory", placeCategory);
        model.addAttribute("translation", translation);

        return "layouts/place_categories/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") long id,
                         @ModelAttribute @Valid CategoryRequest request, BindingResult result,
                         Model model){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            model.addAttribute("errors", errors);
            model.addAttribute("page", "place-category-edit");
            model.addAttribute("title", "Изменить категорию заведения");
            return "layouts/place_categories/edit";
        }

        this.placeCategoryService.update(id, request);

        return "redirect:/place-categories";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") long id){
        this.placeCategoryService.delete(id);
        return "redirect:/place-categories";
    }

    @ResponseBody
    @GetMapping("/getSubcategories/{id}")
    public ResponseEntity<List<PlaceSubCategoryDTO>> getSubCategories(@PathVariable("id") Long id){

        PlaceCategory category =  placeCategoryService.findOne(id);
        List<PlaceSubCategoryDTO> placeSubCategories = category.getSubCategories().stream().map(this::convertToDto).toList();

        return  ResponseEntity.ok(placeSubCategories);
    }

    private PlaceSubCategoryDTO convertToDto(PlaceSubCategory placeSubCategory){
        return this.placeSubCategoriesMapper.toDto(placeSubCategory);
    }
}
