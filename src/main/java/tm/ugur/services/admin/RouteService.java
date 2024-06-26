package tm.ugur.services.admin;

import org.hibernate.Hibernate;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.pagination.PaginationUtil;
import tm.ugur.util.mappers.RouteMapper;

import java.util.*;


@Service
@Transactional(readOnly = true)
public class RouteService {


    private final RouteRepository routeRepository;
    private final EndRouteStopService endRouteStopService;
    private final StartRouteStopService startRouteStopService;
    private final PaginationUtil paginationUtil;
    private final RouteMapper routeMapper;
    private final GeometryFactory factory;

    @Autowired
    public RouteService(RouteRepository routeRepository,
                        EndRouteStopService endRouteStopService,
                        StartRouteStopService startRouteStopService,
                        PaginationUtil paginationUtil,
                        RouteMapper routeMapper,
                        GeometryFactory factory) {
        this.routeRepository = routeRepository;
        this.endRouteStopService = endRouteStopService;
        this.startRouteStopService = startRouteStopService;
        this.paginationUtil = paginationUtil;
        this.routeMapper = routeMapper;
        this.factory = factory;
    }

    public Optional<Route> getRouteByName(String name){
        return routeRepository.findRouteByName(name);
    }

    public Page<Route> getRoutePages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        List<Route> routes = !sortBy.isBlank()
                ? routeRepository.findAll(Sort.by(sortBy)) : routeRepository.findAll();;


        return this.paginationUtil.createPage(routes, pageNumber, itemsPerPage);
    }

    public List<Route> findAll(){
        return routeRepository.findAll();
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationUtil.createPage(routeRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage)
    {
        return paginationUtil.createPage(routeRepository.findAll(), pageNumber, itemsPerPage);
    }

    @Transactional
    public List<Route> findAllHibernateInit(){
        List<Route> routes = routeRepository.findAll();

        routes.forEach(route -> {
            Hibernate.initialize(route.getStartStops());
            Hibernate.initialize(route.getEndStops());
            Hibernate.initialize(route.getStartRouteStops());
            Hibernate.initialize(route.getEndRouteStops());
            Hibernate.initialize(route.getCity().getStops());
        });

        return routes;
    }

    public  Optional<Route> findOne(long id){
        return routeRepository.findById(id);
    }

    public Optional<Route> findByNumber(Integer number){
        return routeRepository.findByNumber(number);
    }

    public Optional<Route> findByNumberInitStops(int number){
        Optional<Route> route = routeRepository.findByNumber(number);
        route.ifPresent(r -> {
            Hibernate.initialize(r.getStartStops());
            Hibernate.initialize(r.getEndStops());
        });

        return route;
    }

    public Optional<Route> findByNumberInitRouteStops(int number){
        Optional<Route> route = routeRepository.findByNumber(number);
        route.ifPresent(r -> {
            Hibernate.initialize(r.getStartRouteStops());
            Hibernate.initialize(r.getEndRouteStops());
            Hibernate.initialize(r.getStartStops());
            Hibernate.initialize(r.getEndStops());
        });

        return route;
    }

    @Transactional
    public void store(Route route, String frontCoordinates, String backCoordinates){
        initializeRoute(route, frontCoordinates, backCoordinates);
        Route newRoute = routeRepository.save(route);
        if(Objects.nonNull(newRoute.getStartStops()))
            startRouteStopService.updateIndexes(newRoute);
        if(Objects.nonNull(route.getEndStops()))
            endRouteStopService.updateIndexes(newRoute);
    }

    @Transactional
    public void store(Route route){
        route.setCreatedAt(new Date());
        route.setUpdatedAt(new Date());
    }

    @Transactional
    public void save(RouteDTO routeDTO){
        Route route = this.converToRoute(routeDTO);
        route.setCreatedAt(new Date());
        route.setUpdatedAt(new Date());
        this.routeRepository.save(route);

    }


    @Transactional
    public void update(long id, Route route, String frontCoordinates, String backCoordinates){
        route.setId(id);
        route.setUpdatedAt(new Date());
        initializeRoute(route, frontCoordinates, backCoordinates);
        Route newRoute = routeRepository.save(route);
        if(Objects.nonNull(newRoute.getStartStops()))
            startRouteStopService.updateIndexes(newRoute);
        if(Objects.nonNull(route.getEndStops()))
            endRouteStopService.updateIndexes(newRoute);
    }

    @Transactional
    public void delete(long id){
        this.routeRepository.deleteById(id);
    }


    private void initializeRoute(Route route, String frontCoordinates, String backCoordinates) {
        route.setFrontLine(getLineString(frontCoordinates));
        route.setBackLine(getLineString(backCoordinates));
        route.setUpdatedAt(new Date());
        if (route.getCreatedAt() == null) {
            route.setCreatedAt(new Date());
        }
        route.setUpdatedAt(new Date());
    }


    private LineString getLineString(String coordinates) {
        String[] points = coordinates.split(",");
        List<Coordinate> coors = new ArrayList<>();
        for (int i = 0; i < points.length - 1; i += 2) {
            String xCoordinate = points[i].replaceAll("LatLng|\\(|\\)", "").trim();
            String yCoordinate = points[i + 1].replaceAll("\\(", "").replaceAll("\\)", "").trim();
            coors.add(new Coordinate(Double.parseDouble(xCoordinate), Double.parseDouble(yCoordinate)));
        }

        return this.factory.createLineString(coors.toArray(Coordinate[]::new));
    }

    private Route converToRoute(RouteDTO routeDTO){
        return routeMapper.toEntity(routeDTO);
    }

}
