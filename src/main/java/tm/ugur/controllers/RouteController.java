package tm.ugur.controllers;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.expression.Numbers;
import tm.ugur.models.Route;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.*;
import tm.ugur.services.api.RouteApiService;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final CityService cityService;
    private final StopService stopService;
    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;


    public RouteController(RouteService routeService, CityService cityService,
                           StopService stopService, StartRouteStopService startRouteStopService,
                           EndRouteStopService endRouteStopService) {
        this.routeService = routeService;
        this.cityService = cityService;
        this.stopService = stopService;
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);
        Page<Route> routes = null;

        if(sortBy != null && sortBy.equals("name")){
            routes = this.routeService.findAll(pageNumber - 1, itemsPerPage, sortBy);
        }else{
            routes = this.routeService.findAll(pageNumber - 1, itemsPerPage);
        }

        int totalPages = routes.getTotalPages();
        int currentPage = routes.getNumber();

        Numbers numbers = new Numbers(Locale.getDefault());
        Integer[] totalPage = numbers.sequence(currentPage > 4 ? currentPage - 1 : 1, currentPage + 4 < totalPages ? currentPage + 3 : totalPages);

        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("title", "Маршруты");
        model.addAttribute("page", "route-index");
        model.addAttribute("routes", routes);
        model.addAttribute("totalPage", totalPage);

        return "layouts/routes/index";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("route") Route route, Model model){
        this.modalAtribitesForCreate(model);
        return "layouts/routes/create";
    }

    @PostMapping
    public String store(@RequestParam(name = "selectedStart") String selectedStart,
                        @RequestParam(name = "selectedEnd") String selectedEnd,
            @ModelAttribute("route") @Valid Route route, BindingResult result, Model model){

        if(result.hasErrors() || selectedStart.isEmpty() || selectedEnd.isEmpty()){
                this.modalAtribitesForCreate(model);
            return "layouts/routes/create";
        }

        this.routeService.store(route);
        this.startRouteStopService.updateIndexs(selectedStart, route);
        this.endRouteStopService.updateIndexs(selectedEnd, route);

        return  "redirect:/routes";
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model){
        model.addAttribute("route", this.routeService.findOne(id));
       this.modalAtribitesForEdit(model);
        return "layouts/routes/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                        @RequestParam(name = "selectedStart") String selectedStart,
                        @RequestParam(name = "selectedEnd") String selectedEnd,
                        @ModelAttribute("route") @Valid Route route, BindingResult result, Model model){

        if(result.hasErrors()){
            this.modalAtribitesForCreate(model);
            return "layouts/routes/edit";
        }

        this.routeService.update(id, route);
        this.startRouteStopService.updateIndexs(selectedStart, route);
        this.endRouteStopService.updateIndexs(selectedEnd, route);

        return  "redirect:/routes";
    }


    private void modalAtribitesForCreate(Model model){
        model.addAttribute("title", "Создать маршрут");
        model.addAttribute("page", "route-craete");
        this.modelForCitiesAndStops(model);
    }

    private void modalAtribitesForEdit(Model model){
        model.addAttribute("title", "Изменить  Маршрут");
        model.addAttribute("page", "route-edit");
        this.modelForCitiesAndStops(model);
    }


    private void modelForCitiesAndStops(Model model){
        model.addAttribute("cities", this.cityService.findAll());
        model.addAttribute("stops", this.stopService.findAll());
    }

    @ModelAttribute("user")
    public boolean isSuperAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Objects.equals(((PersonDetails) auth.getPrincipal()).getUser().getRole().name(), "ROLE_SUPER");
    }
}
