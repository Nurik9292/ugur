package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.models.Stop;
import tm.ugur.services.CityService;
import tm.ugur.services.StopService;

import java.util.Objects;

@Controller
@RequestMapping("/stops")
public class StopController {

    private final StopService stopService;

    private final CityService cityService;

    @Autowired
    public StopController(StopService stopService, CityService cityService) {
        this.stopService = stopService;
        this.cityService = cityService;
    }

    @GetMapping
    public String  index(Model model){
        model.addAttribute("title", "Остановка");
        model.addAttribute("stops", this.stopService.findAll());
        model.addAttribute("page", "stop-index");
        return "layouts/stops/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("stop")Stop stop, Model model){
        model.addAttribute("title", "Создать остановку");
        model.addAttribute("page", "stop-create");
        model.addAttribute("cities", this.cityService.findAll());

        return "layouts/stops/create";
//        return "pages/stops/test";
    }

    @PostMapping("/store")
    public String store(@ModelAttribute("stop") @Valid Stop stop, BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("name"))
                model.addAttribute("nameError", true);
            if(Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("lat") ||
                    Objects.requireNonNull(bindingResult.getFieldError()).getField().equals("lng"))
                model.addAttribute("geo", true);

            model.addAttribute("page", "stop-create");
            model.addAttribute("title", "Создать остановку");

            return "layouts/stops/create";
        }

        this.stopService.store(stop);

        return "redirect:/stops";
    }

}
