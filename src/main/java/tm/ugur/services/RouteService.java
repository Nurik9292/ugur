package tm.ugur.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {


    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Autowired
    public RouteService(RouteRepository routeRepository, RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    public List<Route> findAll(){
        return this.routeRepository.findAll();
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage), this.routeRepository.findAll(), sortBy);
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage)
    {
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage), this.routeRepository.findAll(), "");
    }

    public Route findOne(long id){
        return this.routeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void store(Route route){
        this.routeRepository.save(route);
    }

    @Transactional
    public void save(RouteDTO routeDTO){
        this.routeRepository.save(this.converToRoute(routeDTO));
    }


    @Transactional
    public void update(long id, Route route){
        route.setId(id);
        this.routeRepository.save(route);
    }

    @Transactional
    public void delete(long id){
        this.routeRepository.deleteById(id);
    }


    private Page<Route> findPaginated(Pageable pageable, List<Route> routes, String sortBy){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Route> list;

        if(routes.size() < startItem){
            list = Collections.emptyList();
        }else{
            int toIndex = Math.min(startItem + pageSize, routes.size());
            list = routes.subList(startItem, toIndex);
        }

        if(sortBy.isEmpty()){
            return new PageImpl<Route>(list, PageRequest.of(currentPage, pageSize), routes.size());
        }else{
            return new PageImpl<Route>(list, PageRequest.of(currentPage, pageSize, Sort.by(sortBy)), routes.size());
        }
    }

    private Route converToRoute(RouteDTO routeDTO){
        return this.routeMapper.toEntity(routeDTO);
    }

    private RouteDTO convertToRouteDTO(Route route){
        return this.routeMapper.toDto(route);
    }


    @ExceptionHandler
    private ResponseEntity<RouteErrorResponse> handleException(RouteNotFoundException e){
        RouteErrorResponse errorResponse = new RouteErrorResponse(
                "Route with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
