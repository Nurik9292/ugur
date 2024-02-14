package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.models.Route;
import tm.ugur.util.StaticParams;

import java.util.List;
import java.util.Objects;

@Service
public class RedisRouteService {

    private final RedisTemplate<String, Route> redisTemplate;

    @Autowired
    public RedisRouteService(RedisTemplate<String, Route> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addRoute(Route route){
        redisTemplate.opsForValue().set(
                StaticParams.ROUTE_NUMBER + route.getNumber(), route);
    }

    public Route getRoute(Integer key){
        return redisTemplate.opsForValue().get(Integer.valueOf(StaticParams.ROUTE_NUMBER + key));
    }

    public List<Route> getRoutes(){
        return redisTemplate.opsForValue().multiGet(Objects.requireNonNull(redisTemplate.keys("*")));
    }

    public void deleteRoute(Integer key){
        redisTemplate.delete(StaticParams.ROUTE_NUMBER + key);
    }
}
