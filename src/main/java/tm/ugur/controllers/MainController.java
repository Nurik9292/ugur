package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import tm.ugur.dto.BusDTO;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.admin.BusSservice;
import tm.ugur.services.admin.ClientService;
import tm.ugur.services.redis.RedisBusService;

import java.util.List;
import java.util.Objects;

@Controller
public class MainController {

    private final ClientService clientService;
    private final RedisBusService redisBusService;


    @Autowired
    public MainController(ClientService clientService, RedisBusService redisBusService) {
        this.clientService = clientService;
        this.redisBusService = redisBusService;
    }

    @GetMapping("/")
    public String mainPage(Model model){
        List<BusDTO> buses = redisBusService.getAll();

        model.addAttribute("title", "Главная");
        model.addAttribute("page", "main");
        model.addAttribute("clientCount", this.clientService.getAll().size());
        model.addAttribute("busCount", buses.size());
        model.addAttribute("busOnline", buses.stream().filter(BusDTO::isStatus).toList().size());
        return "layouts/base";
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
