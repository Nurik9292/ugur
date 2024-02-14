package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Stop;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.admin.CityService;
import tm.ugur.services.admin.StopService;
import tm.ugur.util.errors.stop.StopErrorResponse;
import tm.ugur.util.errors.stop.StopNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/stops")
public class StopController {

    private final StopService stopService;
    private final CityService cityService;

    private static String sortByStatic = "";

    @Autowired
    public StopController(StopService stopService, CityService cityService) {
        this.stopService = stopService;
        this.cityService = cityService;
    }

    @GetMapping
    public String  index(@RequestParam(name = "page", required = false) String page,
                         @RequestParam(name = "items", required = false) String items,
                         @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        if(sortBy != null){
            sortByStatic = sortBy;
        }

        Page<Stop> stops = this.stopService.getStopPages(page, items, sortByStatic);
        int totalPages = stops.getTotalPages();
        Integer[] totalPage = this.stopService.getTotalPage(totalPages, stops.getNumber());

        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("title", "Остановка");
        model.addAttribute("stops", stops);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("page", "stop-index");
        return "layouts/stops/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("stop")Stop stop, Model model){
        sortByStatic = "";
        model.addAttribute("title", "Создать остановку");
        model.addAttribute("page", "stop-create");
        model.addAttribute("cities", this.cityService.findAll());

        return "layouts/stops/create";
    }

    @PostMapping
    public String store(@ModelAttribute("stop") @Valid Stop stop, BindingResult result, Model model){

        if (result.hasErrors()){
            this.errors(model, result);
            model.addAttribute("page", "stop-create");
            model.addAttribute("title", "Создать остановку");
            model.addAttribute("cities", this.cityService.findAll());
            return "layouts/stops/create";
        }

        this.stopService.store(stop);

        return "redirect:/stops";
    }




    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model){
        sortByStatic = "";
        model.addAttribute("title", "Изменить Остановку");
        model.addAttribute("page", "stop-edit");
        model.addAttribute("stop", this.stopService.findOne(id));
        model.addAttribute("cities", this.cityService.findAll());

        return "layouts/stops/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("stop") @Valid Stop stop, BindingResult result,
                         Model model){

        if(result.hasErrors()){
            this.errors(model, result);
            model.addAttribute("page", "stop-create");
            model.addAttribute("title", "Создать остановку");
            return "layouts/stops/edit";
        }

        this.stopService.update(id, stop);

        return "redirect:/stops";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id){
        this.stopService.delete(id);
        return "redirect:/stops";
    }

    private void errors(Model model, BindingResult result){
        if(Objects.requireNonNull(result.getFieldError()).getField().equals("name"))
            model.addAttribute("nameError", true);
        if(Objects.requireNonNull(result.getFieldError()).getField().equals("lat") ||
                Objects.requireNonNull(result.getFieldError()).getField().equals("lng"))
            model.addAttribute("geo", true);
    }


    @ExceptionHandler
    private ResponseEntity<StopErrorResponse> handleException(StopNotFoundException e){
        StopErrorResponse errorResponse = new StopErrorResponse(
                "Stop with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
