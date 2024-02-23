package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Route;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.redis.RedisRouteService;

import java.util.List;

@Component
@Order(7)
@ConditionalOnProperty(name = "db.init.enabled", havingValue = "true", matchIfMissing = false)
public class RouteCacheSeed implements CommandLineRunner {

    private final RedisRouteService redisRouteService;
    private final RouteService routeService;

    @Autowired
    public RouteCacheSeed(RedisRouteService redisRouteService, RouteService routeService) {
        this.redisRouteService = redisRouteService;
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Route> routes = routeService.findAllHibernateInit();

        for (Route route :routes){
            redisRouteService.addRoute(route);
        }
    }
}
