package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;

import java.util.List;


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
        System.out.println(this.routeRepository.findAll().getFirst().getStartStops());
        return this.routeRepository.findAll().stream().map(this::convertToRouteDTO).toList();
    }



    public RouteDTO findOne(long id){
        return this.convertToRouteDTO(
                this.routeRepository.findById(Long.valueOf(id)).orElseThrow(RouteNotFoundException::new));
    }



    public Route converToRoute(RouteDTO routeDTO){
        return this.routeMapper.toEntity(routeDTO);
    }

    public RouteDTO convertToRouteDTO(Route route){
        return this.routeMapper.toDto(route);
    }

}
