package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.EndRouteStopService;
import tm.ugur.services.RouteService;
import tm.ugur.services.StartRouteStopService;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class TestController {

    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;
    private final RouteService routeService;

    @Autowired
    public TestController(StartRouteStopService startRouteStopService, EndRouteStopService endRouteStopService, RouteService routeService) {
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
        this.routeService = routeService;
    }

    @GetMapping("/test")
    public String test(){
        File resource = new File("stop.json"); // Путь к входному файлу
        File outputFile = new File("stop.json"); // Путь к выходному файлу

        try (BufferedReader reader = new BufferedReader(new FileReader(resource));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("lngs", "lat");
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Замена выполнена успешно.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @GetMapping("/index-start")
    public void indexingStart(){
        Set<Route> routes = new HashSet<>(routeService.findAll());

        routes.forEach(route -> {
            AtomicInteger count = new AtomicInteger(2);
           List<StartRouteStop> startRouteStops = startRouteStopService.findByRoute(route);
           startRouteStops.forEach(startRouteStop -> {
               startRouteStop.setIndex(count.get());
               startRouteStopService.store(startRouteStop);
               count.addAndGet(2);
           });
        });
    }

    @GetMapping("/index-end")
    public void indexingEnd(){
        List<Route> routes = routeService.findAll();

        routes.forEach(route -> {
            AtomicInteger count = new AtomicInteger(1);
            List<EndRouteStop> endRouteStops = endRouteStopService.findByRoute(route);
            endRouteStops.forEach(endRouteStop -> {
                endRouteStop.setIndex(count.get());
                endRouteStopService.store(endRouteStop);
                count.addAndGet(2);
            });
        });
    }
}
