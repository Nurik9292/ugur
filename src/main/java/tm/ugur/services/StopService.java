package tm.ugur.services;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Stop;
import tm.ugur.pojo.CustomPoint;
import tm.ugur.repo.StopRepository;
import tm.ugur.util.errors.stop.StopNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class StopService {

    private final StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<Stop> findAll(){
        return this.stopRepository.findAll();
    }

    public Page<Stop> findAll(int pageNumber,  int itemsPerPage){
        List<Stop> stops = this.stopRepository.findAll();
        stops.forEach(this::setLatLng);
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage), stops, "");
    }

    public Page<Stop> findAll(int pageNumber,  int itemsPerPage, String sortBy){
        List<Stop> stops = this.stopRepository.findAll();
        stops.forEach(this::setLatLng);
        return this.findPaginated(PageRequest.of(pageNumber, itemsPerPage, Sort.by(sortBy)), stops, sortBy);
    }

    public Page<Stop> search(String name){
        List<Stop> stops = this.stopRepository.findByNameStartingWith(name);
        stops.forEach(this::setLatLng);
        return this.findPaginated(PageRequest.of(0, 10), stops, "");
    }

    public Stop findOne(int id){
        Stop stop = this.stopRepository.findById(id).orElseThrow(StopNotFoundException::new);
        this.setLatLng(Objects.requireNonNull(stop));
        return stop;
    }

    @Transactional
    public void store(Stop stop){
        stop.setLocation("{\"lat\": " + stop.getLat()  + ", " +  "\"lng\": " + stop.getLng() + "}");
        this.stopRepository.save(stop);
    }

    @Transactional
    public void update(int id, Stop stop){
        stop.setLocation("{\"lat\": " + stop.getLat()  + ", " +  "\"lng\": " + stop.getLng() + "}");
        stop.setId(id);
        this.stopRepository.save(stop);
    }



    @Transactional
    public void delete(int id){
        this.stopRepository.deleteById(id);
    }

    private void setLatLng(Stop stop){
        Gson gson = new Gson();
        CustomPoint point = gson.fromJson(stop.getLocation(), CustomPoint.class);
        stop.setLat(point.getLat());
        stop.setLng(point.getLng());
    }

    private Page<Stop> findPaginated(Pageable pageable, List<Stop> stops, String sortBy){
       int pageSize = pageable.getPageSize();
       int currentPage = pageable.getPageNumber();
       int startItem = currentPage * pageSize;

       List<Stop> list;

       if(stops.size() < startItem){
           list = Collections.emptyList();
       }else{
           int toIndex = Math.min(startItem + pageSize, stops.size());
           list = stops.subList(startItem, toIndex);
       }

       if(sortBy.isEmpty()){
           return new PageImpl<Stop>(list, PageRequest.of(currentPage, pageSize), stops.size());
       }else{
           return new PageImpl<Stop>(list, PageRequest.of(currentPage, pageSize, Sort.by(sortBy)), stops.size());
       }
    }



}
