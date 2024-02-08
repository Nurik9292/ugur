package tm.ugur.services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.expression.Numbers;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.pagination.PaginationService;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class RouteService {


    private final RouteRepository routeRepository;
    private final PaginationService paginationService;
    private final RouteMapper routeMapper;
    private final GeometryFactory factory;

    @Autowired
    public RouteService(RouteRepository routeRepository, PaginationService paginationService, RouteMapper routeMapper, GeometryFactory factory) {
        this.routeRepository = routeRepository;
        this.paginationService = paginationService;
        this.routeMapper = routeMapper;
        this.factory = factory;
    }

    public Page<Route> getRoutePages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        List<Route> routes = !sortBy.isBlank()
                ? this.routeRepository.findAll(Sort.by(sortBy)) : this.routeRepository.findAll();;


        return this.paginationService.createPage(routes, pageNumber, itemsPerPage);
    }


    public List<Route> findAll(){
        return this.routeRepository.findAll();
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.paginationService.createPage(this.routeRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage)
    {
        return this.paginationService.createPage(this.routeRepository.findAll(), pageNumber, itemsPerPage);
    }

    public  Optional<Route> findOne(long id){
        return this.routeRepository.findById(id);
    }

    @Transactional
    public void store(Route route, String frontCoordinates, String backCoordinates){
        route.setFrontLine(this.getLineString(frontCoordinates));
        route.setBackLine(this.getLineString(backCoordinates));
        route.setUpdatedAt(new Date());
        route.setCreatedAt(new Date());
        this.routeRepository.save(route);
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

        LineString frontLine = !frontCoordinates.isEmpty() ? getLineString(frontCoordinates) :
                Objects.requireNonNull(this.routeRepository.findById(id).orElse(null)).getFrontLine();
        route.setFrontLine(frontLine);

        LineString backLine = !backCoordinates.isEmpty() ? getLineString(backCoordinates) :
                Objects.requireNonNull(this.routeRepository.findById(id).orElse(null)).getBackLine();
        route.setBackLine(backLine);

        this.routeRepository.save(route);
    }

    @Transactional
    public void delete(long id){
        this.routeRepository.deleteById(id);
    }


    public Optional<Route> findRoutesByClient(Client client, Long id){
        return this.routeRepository.findRouteByClientsAndId(client, id);
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

    public Integer[] getTotalPage(int totalPages, int currentPage){
        Numbers numbers = new Numbers(Locale.getDefault());
        return numbers.sequence(currentPage > 4 ? currentPage - 1 : 1, currentPage + 4 < totalPages ? currentPage + 3 : totalPages);
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
