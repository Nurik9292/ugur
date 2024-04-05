package tm.ugur.services.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.util.Constant;
import tm.ugur.util.mappers.RouteMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RedisRouteService {

    private final RedisTemplate<String, RouteDTO> redisTemplate;
    private final RouteMapper routeMapper;

    @Autowired
    public RedisRouteService(RedisTemplate<String, RouteDTO> redisTemplate, RouteMapper routeMapper) {
        this.redisTemplate = redisTemplate;
        this.routeMapper = routeMapper;
    }

    public void addRoute(Route route){
        redisTemplate.opsForValue().set(Constant.ROUTE_NUMBER + route.getNumber(), convertToDto(route));
    }

    public void addRoute(RouteDTO route){
        redisTemplate.opsForValue().set(Constant.ROUTE_NUMBER + route.getNumber(), route);
    }

    public Route getRoute(String key){
        return convertToEntity(redisTemplate.opsForValue().get(Constant.ROUTE_NUMBER + key));
    }

    public List<Route> getEntityRoutes() {
        return redisTemplate.opsForValue()
                .multiGet(Objects.requireNonNull(redisTemplate.keys(Constant.ROUTE_NUMBER + "*")))
                .stream().map(this::convertToEntity).collect(Collectors.toList());

    }

    public List<RouteDTO> getRoutes() {
        return redisTemplate.opsForValue()
                .multiGet(Objects.requireNonNull(redisTemplate.keys(Constant.ROUTE_NUMBER + "*")));

    }



    public void deleteRoute(String key){
        redisTemplate.delete(Constant.ROUTE_NUMBER + key);
    }

    private Route convertToEntity(RouteDTO routeDTO){
        return routeMapper.toEntity(routeDTO);
    }

    protected RouteDTO convertToDto(Route route) {
        return routeMapper.toDto(route);
    }
}
