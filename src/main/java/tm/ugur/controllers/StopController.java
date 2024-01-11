package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import tm.ugur.services.StopService;

@Controller
public class StopController {

    private final StopService stopService;

    @Autowired
    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    public String  index(Model model){
        model.addAttribute("title", "Остановка");
        model.addAttribute("stops", this.stopService.findAll());

        return "layouts/stops/index";
    }

}
