package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.admin.BusSservice;
import tm.ugur.services.admin.ClientService;

import java.util.Objects;

@Controller
public class MainController {

    private final ClientService clientService;
    private final BusSservice busSservice;

    @Autowired
    public MainController(ClientService clientService, BusSservice busSservice) {
        this.clientService = clientService;
        this.busSservice = busSservice;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("title", "Главная");
        model.addAttribute("page", "main");
        model.addAttribute("clientCount", this.clientService.getAll().size());
        model.addAttribute("busCount", this.busSservice.findAll().size());
        return "layouts/base";
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
