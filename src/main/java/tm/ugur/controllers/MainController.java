package tm.ugur.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import tm.ugur.security.PersonDetails;

import java.util.Objects;

@Controller
public class MainController {


    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("title", "Главная");
        model.addAttribute("page", "main");
        return "layouts/base";
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
