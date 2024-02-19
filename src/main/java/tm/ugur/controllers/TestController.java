package tm.ugur.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.admin.EndRouteStopService;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.admin.StartRouteStopService;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.redis.RedisRouteService;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class TestController {

    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;
    private final RouteService routeService;
    private final StopService stopService;
    private final RedisRouteService redisRouteService;


    @Autowired
    public TestController(StartRouteStopService startRouteStopService,
                          EndRouteStopService endRouteStopService,
                          RouteService routeService,
                          StopService stopService,
                          RedisRouteService redisRouteService) {
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
        this.routeService = routeService;
        this.stopService = stopService;
        this.redisRouteService = redisRouteService;
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

    @GetMapping("/test-location")
    public void testLocation(){
        System.out.println(stopService.findNearestStops(37.95102886133695, 58.371306048883476));
    }

    @GetMapping("/redis-routes")
    public void testRedisRoutes(){
        System.out.println(routeService.findOne(2).get().getEndRouteStops());
        System.out.println(redisRouteService.getRoutes().getFirst().getEndRouteStops());
    }
}
