package tm.ugur.services.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.errors.route.RouteNotFoundException;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class RouteApiService {
    private final RouteRepository routeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RouteApiService(RouteRepository routeRepository, ModelMapper modelMapper) {
        this.routeRepository = routeRepository;
        this.modelMapper = modelMapper;
    }


    public List<RouteDTO> findAll(){
        return this.routeRepository.findAll().stream().map(this::convertToRouteDTO).toList();
    }



    public RouteDTO findOne(int id){
        return this.convertToRouteDTO(
                this.routeRepository.findById(Long.valueOf(id)).orElseThrow(RouteNotFoundException::new));
    }



    public Route converToRoute(RouteDTO routeDTO){
        return this.modelMapper.map(routeDTO, Route.class);
    }

    public RouteDTO convertToRouteDTO(Route route){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(route, RouteDTO.class);
    }

}
