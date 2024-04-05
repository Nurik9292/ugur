package tm.ugur.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import tm.ugur.dto.RouteDTO;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.services.redis.RedisRouteService;

import java.util.List;

@Order(20)
public class RouteRedisSeed implements CommandLineRunner {

    private final RouteApiService routeService;
    private final RedisRouteService redisRouteService;



    public RouteRedisSeed(RouteApiService routeService, RedisRouteService redisRouteService) {
        this.routeService = routeService;
        this.redisRouteService = redisRouteService;
    }

    @Override
    public void run(String... args) throws Exception {
            List<RouteDTO> routes = routeService.findAll();
            routes.forEach(redisRouteService::addRoute);
    }
}
