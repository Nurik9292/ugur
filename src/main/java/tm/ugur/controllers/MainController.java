package tm.ugur.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {


    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("title", "Главная");
        model.addAttribute("page", "main");
        return "layouts/base";
    }
}
