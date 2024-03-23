package tm.ugur.services.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.repo.RouteRepository;
import tm.ugur.security.ClientDetails;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;
import tm.ugur.util.mappers.StopMapper;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class RouteApiService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final StopMapper stopMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public RouteApiService(RouteRepository routeRepository, RouteMapper routeMapper, StopMapper stopMapper, ObjectMapper objectMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
        this.stopMapper = stopMapper;
        this.objectMapper = objectMapper;
    }


    public List<RouteDTO> findAll(){
        List<RouteDTO> routeDTOS = this.routeRepository
                .findAllWithIdNameIntervalNumberCityRouteTime().stream().map(this::convertToRouteDTO).toList();
        routeDTOS.forEach(routeDTO -> routeDTO.setIs_favorite(this.isFavorite(routeDTO)));
        return routeDTOS;
    }

    public List<RouteDTO> getRoutes(){
        return this.routeRepository.findAll().stream().map(this::convertToRouteDTO).toList();
    }


    public Optional<Route> getRoute(Long id){
        return this.routeRepository.findById(id);
    }

    public RouteDTO findOne(Long id){
        RouteDTO routeDTO = this.convertToRouteDTO(
                this.routeRepository.findById(Long.valueOf(id)).orElseThrow(RouteNotFoundException::new));
        routeDTO.setIs_favorite(isFavorite(routeDTO));
        return routeDTO;
    }


    public String getRouteStartStop(Long id){
        Optional<Route> route =  getRoute(id);
        try {

         List<StopDTO> stopDTOS =  route.orElseThrow(RouteNotFoundException::new).getStartStops().stream().map(this::convertToStopDTO).toList();

            return objectMapper.writer(filters()).writeValueAsString(stopDTOS);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public String getRouteEndStop(Long id){
        Optional<Route> route =  getRoute(id);
        try {
            List<StopDTO> stopDTOS =  route.orElseThrow(RouteNotFoundException::new).getEndStops().stream().map(this::convertToStopDTO).toList();

            return objectMapper.writer(filters()).writeValueAsString(stopDTOS);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    private FilterProvider filters(){
        FilterProvider filter = new SimpleFilterProvider()
                .addFilter("stopFilter", SimpleBeanPropertyFilter
                        .filterOutAllExcept("id", "name", "location"));

        return filter;
    }

    @Transactional
    public void store(Route route){
        this.routeRepository.save(route);
    }

    public Route converToRoute(RouteDTO routeDTO){
        return routeMapper.toEntity(routeDTO);
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


    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
