package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.City;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.CityService;

import java.util.Objects;

@Controller
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("title", "Города");
        model.addAttribute("page", "city-index");
        model.addAttribute("cities", this.cityService.findAll());

        return "layouts/cities/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("clity") City city, Model model){
        model.addAttribute("title", "Создание города");
        model.addAttribute("page", "city-create");

        return "layouts/cities/create";
    }

    @PostMapping("/store")
    public String  store(@ModelAttribute("city") @Valid City city, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("name"))
                model.addAttribute("nameError", true);
            model.addAttribute("page", "city-create");
            model.addAttribute("title", "Создать пользователя");

            return "layouts/cities/create";
        }

        this.cityService.store(city);
        return "redirect:/cities";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        this.cityService.delete(id);
        return "redirect:/cities";
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
