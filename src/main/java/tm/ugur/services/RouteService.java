package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }


    public List<Route> findAll(){
        return this.routeRepository.findAll();
    }


    public Route findOne(long id){
        return this.routeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void store(Route route){
        this.routeRepository.save(route);
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
}
