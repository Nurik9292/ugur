package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.*;
import tm.ugur.repo.RouteRepository;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.redis.RedisBusService;
import tm.ugur.services.redis.RedisRouteService;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;
import tm.ugur.util.mappers.StopMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class RouteApiService {

    private final RouteRepository routeRepository;
    private final RedisBusService redisBusService;
    private final RedisRouteService redisRouteService;
    private final RouteMapper routeMapper;
    private final StopMapper stopMapper;

    @Autowired
    public RouteApiService(RouteRepository routeRepository,
                           RedisBusService redisBusService, RedisRouteService redisRouteService,
                           RouteMapper routeMapper,
                           StopMapper stopMapper) {
        this.routeRepository = routeRepository;
        this.redisBusService = redisBusService;
        this.redisRouteService = redisRouteService;
        this.routeMapper = routeMapper;
        this.stopMapper = stopMapper;
    }


    public List<RouteDTO> findAll(){
        List<RouteDTO> routeDTOS = routeRepository
                .findAllWithIdNameIntervalNumberCityRouteTime().stream().map(this::convertToRouteDTO).toList();
        routeDTOS.forEach(routeDTO -> {
            routeDTO.setIs_favorite(isFavorite(routeDTO));
            routeDTO.setInterval(getInterval(routeDTO) + " min");
        });
        return routeDTOS;
    }

    public List<RouteDTO> getRoutes(){
        return routeRepository.findAll().stream().map(this::convertToRouteDTO).toList();
    }


    public Optional<Route> getRoute(Long id){
        return this.routeRepository.findById(id);
    }

    public RouteDTO findOne(Long id){
        return convertToRouteDTO(
                routeRepository.findById(Long.valueOf(id)).orElseThrow(RouteNotFoundException::new));
    }


    public List<StopDTO> getRouteStartStop(Long id){
        Optional<Route> route =  getRoute(id);
         return route.orElseThrow(RouteNotFoundException::new)
                 .getStartRouteStops().stream()
                 .sorted(Comparator.comparing(StartRouteStop::getIndex))
                 .map(rs -> convertToStopDTO(rs.getStop()))
                 .toList();
    }

    public List<StopDTO> getRouteEndStop(Long id){
        Optional<Route> route =  getRoute(id);
        return route.orElseThrow(RouteNotFoundException::new)
                .getEndRouteStops().stream()
                .sorted(Comparator.comparing(EndRouteStop::getIndex))
                .map(rs -> convertToStopDTO(rs.getStop())).toList();

    }

    @Transactional
    public void store(Route route){
        this.routeRepository.save(route);
    }


    public RouteDTO convertToRouteDTO(Route route){
        return routeMapper.toDto(route);
    }

    public StopDTO convertToStopDTO(Stop stop){
        return stopMapper.toDto(stop);
    }

    private boolean isFavorite(RouteDTO routeDTO){
        Client client = getAuthClient();
        return client.getRoutes().stream().anyMatch(route -> route.getId().equals(routeDTO.getId()));
    }

    private int getInterval(RouteDTO routeDTO){

        List<BusDTO> buses = redisBusService.getBuses(String.valueOf(routeDTO.getNumber()));


        if(buses == null)
            return 0;

        System.out.println(buses);
        System.out.println(routeDTO.getRoutingTime());

        return routeDTO.getRoutingTime() / buses.size();
    }


    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
