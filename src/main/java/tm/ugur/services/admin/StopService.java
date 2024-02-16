package tm.ugur.services.admin;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.expression.Numbers;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Stop;
import tm.ugur.repo.CityRepository;
import tm.ugur.repo.StopRepository;
import tm.ugur.util.pagination.PaginationService;
import tm.ugur.util.errors.stop.StopNotFoundException;
import tm.ugur.util.mappers.StopMapper;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class StopService {

    private final PaginationService paginationService;
    private final StopRepository stopRepository;
    private final CityRepository cityRepository;
    private final GeometryFactory factory;
    private final StopMapper stopMapper;

    @Autowired
    public StopService(PaginationService paginationService,
                       StopRepository stopRepository,
                       CityRepository cityRepository,
                       GeometryFactory factory,
                       StopMapper stopMapper) {
        this.paginationService = paginationService;
        this.stopRepository = stopRepository;
        this.cityRepository = cityRepository;
        this.factory = factory;
        this.stopMapper = stopMapper;
    }

    public Page<Stop> getStopPages(String page, String items, String sortBy){
        int pageNumber = page == null ? 1 : Integer.parseInt(page);
        int itemsPerPage = items == null ? 10 : Integer.parseInt(items);

        List<Stop> stops =  sortBy.equals("name")
                ? this.stopRepository.findAll(Sort.by(sortBy)) : this.stopRepository.findAll();


        return paginationService.createPage(stops, pageNumber, itemsPerPage);

    }

    public List<Stop> findAll(){
        return this.stopRepository.findAll();
    }

    public Page<Stop> findAll(int pageNumber,  int itemsPerPage){
        List<Stop> stops = this.stopRepository.findAll();
        stops.forEach(this::setLatLng);
        return paginationService.createPage(stopRepository.findAll(), pageNumber, itemsPerPage);
    }

    public Page<Stop> findAll(int pageNumber,  int itemsPerPage, String sortBy){
        List<Stop> stops = this.stopRepository.findAll();
        stops.forEach(this::setLatLng);
        return paginationService.createPage(stopRepository.findAll(Sort.by(sortBy)), pageNumber, itemsPerPage);
    }


    public Stop findOne(Long id){
        Stop stop = this.stopRepository.findById(id).orElseThrow(StopNotFoundException::new);
        this.setLatLng(Objects.requireNonNull(stop));
        return stop;
    }

    public List<Stop> findNearestStops(double lat, double lng){
        return stopRepository.findNearestStops(lat, lng);
    }

    public Boolean hasRouteByName(String name){
        return this.stopRepository.existsByName(name);
    }


    @Transactional
    public void store(Stop stop){
        stop.setLocation(this.factory.createPoint(new Coordinate(stop.getLat(), stop.getLng())));
        stop.setCreatedAt(new Date());
        stop.setUpdatedAt(new Date());
        this.stopRepository.save(stop);
    }

    @Transactional
    public void save(StopDTO stopDTO){
        Stop stop = new Stop();
        stop.setCreatedAt(new Date());
        stop.setUpdatedAt(new Date());
        stop.setName(stopDTO.getName());
        stop.setLocation(this.factory.createPoint(new Coordinate(stopDTO.getLocation().getLat(), stopDTO.getLocation().getLng())));
        stop.setCity(this.cityRepository.findById(stopDTO.getCity().getId()).orElse(null));
        this.stopRepository.save(stop);
    }


    @Transactional
    public void update(Long id, Stop stop){
        stop.setId(id);
        stop.setUpdatedAt(new Date());
        stop.setLocation(this.factory.createPoint(new Coordinate(stop.getLat(), stop.getLng())));
        this.stopRepository.save(stop);
    }

    public Optional<Stop> findStopByName(String name){
        return this.stopRepository.findByName(name);
    }

    @Transactional
    public void delete(Long id){
        this.stopRepository.deleteById(id);
    }



    private void setLatLng(Stop stop){
        Point point = stop.getLocation();
        stop.setLat(point.getX());
        stop.setLng(point.getY());
    }


    public Integer[] getTotalPage(int totalPages, int currentPage){
        Numbers numbers = new Numbers(Locale.getDefault());
        return numbers.sequence(currentPage > 4 ? currentPage - 1 : 1, currentPage + 4 < totalPages ? currentPage + 3 : totalPages);
    }


    public StopDTO convertToStopDTO(Stop stop){
        return this.stopMapper.toDto(stop);
    }

    public Stop convertToStop(StopDTO stopDTO) {
        return this.stopMapper.toEntity(stopDTO);
    }

}
