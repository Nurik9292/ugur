package tm.ugur.controllers;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Route;
import tm.ugur.security.PersonDetails;
import tm.ugur.services.admin.*;
import tm.ugur.errors.route.RouteErrorResponse;
import tm.ugur.errors.route.RouteNotFoundException;
import tm.ugur.util.pagination.PaginationUtil;

import java.util.*;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final CityService cityService;
    private final StopService stopService;
    private final PaginationUtil paginationUtil;
    private final EndRouteStopService endRouteStopService;
    private final StartRouteStopService startRouteStopService;
    private static String sortByStatic = "";


    public RouteController(RouteService routeService, CityService cityService,
                           StopService stopService,
                           PaginationUtil paginationUtil,
                           StartRouteStopService startRouteStopService,
                           EndRouteStopService endRouteStopService) {
        this.routeService = routeService;
        this.cityService = cityService;
        this.stopService = stopService;
        this.paginationUtil = paginationUtil;
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
    }

    @GetMapping
    public String index(@RequestParam(name = "page", required = false) String page,
                        @RequestParam(name = "items", required = false) String items,
                        @RequestParam(value = "sortBy", required = false) String sortBy, Model model){

        if(sortBy != null){
            sortByStatic = sortBy;
        }

        Page<Route> routes = this.routeService.getRoutePages(page, items, sortByStatic);
        int totalPages = routes.getTotalPages();
        Integer[] totalPage = this.paginationUtil.getTotalPage(totalPages, routes.getNumber());

        if(routes.getTotalPages() > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, routes.getTotalPages()).boxed().toList();
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
        sortByStatic = "";
        this.modalAttributesForCreate(model);
        return "layouts/routes/create";
    }

    @PostMapping
    public ResponseEntity<?> store(
            @RequestParam(name = "frontCoordinates", required = false) String frontCoordinates,
            @RequestParam(name = "backCoordinates", required = false) String backCoordinates,
            @ModelAttribute("route") @Valid Route route, BindingResult result){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        this.routeService.store(route, frontCoordinates, backCoordinates);

        return  ResponseEntity.ok("Маршрут успешно добавленно!");
    }


    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model){
        sortByStatic = "";
        Route route = this.routeService.findOne(id).orElse(null);
        model.addAttribute("route", route);
        this.modalAttributesForEdit(model);
        return "layouts/routes/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                         @RequestParam(name = "frontCoordinates", required = false) String frontCoordinates,
                         @RequestParam(name = "backCoordinates", required = false) String backCoordinates,
                        @ModelAttribute("route") @Valid Route route, BindingResult result, Model model){

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

//        route.getEndStops().contains()
        this.routeService.update(id, route, frontCoordinates, backCoordinates);

        return  ResponseEntity.ok("Маршрут успешно обновленно!");
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        this.routeService.delete(id);
        return "redirect:/routes";
    }


    private void modalAttributesForCreate(Model model){
        model.addAttribute("title", "Создать маршрут");
        model.addAttribute("page", "route-create");
        this.modelForCitiesAndStops(model);
    }

    private void modalAttributesForEdit(Model model){
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

    @ExceptionHandler
    private ResponseEntity<RouteErrorResponse> handleException(RouteNotFoundException e){
        RouteErrorResponse errorResponse = new RouteErrorResponse(
                "Route with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
