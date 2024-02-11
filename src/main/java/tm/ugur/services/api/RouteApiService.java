package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.security.ClientDetails;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class RouteApiService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Autowired
    public RouteApiService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }


    public List<RouteDTO> findAll(){
        List<RouteDTO> routeDTOS = this.routeRepository
                .findAllWithIdNameIntervalNumberCityRouteTime().stream().map(this::convertToRouteDTO).toList();
        routeDTOS.forEach(routeDTO -> routeDTO.setFavorite(this.isFavorite(routeDTO)));
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
        routeDTO.setFavorite(isFavorite(routeDTO));
        return routeDTO;
    }

    @Transactional
    public void store(Route route){
        this.routeRepository.save(route);
    }

    public Route converToRoute(RouteDTO routeDTO){
        return this.routeMapper.toEntity(routeDTO);
    }

    public RouteDTO convertToRouteDTO(Route route){
        return this.routeMapper.toDto(route);
    }

    private boolean isFavorite(RouteDTO routeDTO){
        Client client = getAuthClient();
        return client.getRoutes().stream().anyMatch(route -> route.getId() == routeDTO.getId());
    }


    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
