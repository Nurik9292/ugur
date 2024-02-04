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
import tm.ugur.models.Route;
import tm.ugur.repo.RouteRepository;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.RouteMapper;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class RouteService {


    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final GeometryFactory factory;

    @Autowired
    public RouteService(RouteRepository routeRepository, RouteMapper routeMapper, GeometryFactory factory) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
        this.factory = factory;
    }

    public Page<Route> getRoutePages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        Page<Route> routes = null;

        if(sortBy != null && sortBy.equals("name") || sortBy != null && sortBy.equals("number")){
            routes = this.findAll(pageNumber - 1, itemsPerPage, sortBy);
        }else{
                routes = this.findAll(pageNumber - 1, itemsPerPage);
        }

        return routes;
    }


    public List<Route> findAll(){
        return this.routeRepository.findAll();
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage, String sortBy)
    {
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage), this.routeRepository.findAll(Sort.by(sortBy)));
    }

    public Page<Route> findAll(int pageNumber, int itemsPerPage)
    {
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage), this.routeRepository.findAll());
    }

    public Route findOne(long id){
        return this.routeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void store(Route route, String frontCoordinates, String backCoordinates){
        route.setFrontLine(this.getLineString(frontCoordinates));
        route.setBackLine(this.getLineString(backCoordinates));
        this.routeRepository.save(route);
    }

    @Transactional
    public void save(RouteDTO routeDTO){
        this.routeRepository.save(this.converToRoute(routeDTO));
    }


    @Transactional
    public void update(long id, Route route, String frontCoordinates, String backCoordinates){
        route.setId(id);

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


    private Page<Route> findPaginated(Pageable pageable, List<Route> routes){
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

            return new PageImpl<Route>(list, PageRequest.of(currentPage, pageSize), routes.size());
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
