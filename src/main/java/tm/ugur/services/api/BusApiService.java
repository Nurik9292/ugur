package tm.ugur.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.*;
import tm.ugur.repo.BusRepository;
import tm.ugur.repo.RouteRepository;
import tm.ugur.services.redis.RedisBusService;
import tm.ugur.util.errors.buses.BusNotFoundException;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.util.mappers.BusMapper;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BusApiService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final RedisBusService redisBusService;
    private final BusMapper busMapper;

    @Autowired
    public BusApiService(BusRepository busRepository,
                         RouteRepository routeRepository,
                         RedisBusService redisBusService,
                         BusMapper busMapper) {
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.redisBusService = redisBusService;
        this.busMapper = busMapper;
    }

    public List<BusDTO> getBuses(){
        return this.busRepository.findAll().stream().map(this::convertToBusDTO).toList();
    }

    public BusDTO getBus(long id){
        Bus bus = this.busRepository.findById(id).orElseThrow(BusNotFoundException::new);
        return this.convertToBusDTO(bus);
    }

    public String getNextStop(long id, String carNumber){
        Route route = routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
        System.out.println(route.getNumber() + " " + carNumber);
        BusDTO bus =  redisBusService.getBuses(
                String.valueOf(route.getNumber()))
                .stream().filter(b -> b.getCarNumber().equals(carNumber))
                .findAny().orElseThrow(BusNotFoundException::new);

        Stop stop;
        if (bus.getSide().equals("front")) {
            stop = route.getStartRouteStops()
                    .stream()
                    .filter(s -> s.getIndex() > bus.getIndex())
                    .map(StartRouteStop::getStop)
                    .findFirst()
                    .orElse(null);
        } else {
            stop = route.getEndRouteStops()
                    .stream()
                    .filter(s -> s.getIndex() > bus.getIndex())
                    .map(EndRouteStop::getStop)
                    .findFirst()
                    .orElse(null);
        }

        return stop !=  null ? stop.getName() : "Stop not found";
     }


    @Transactional
    public void store(Bus bus){
        this.busRepository.save(bus);
    }

    @Transactional
    public void update(long id, Bus bus){
        bus.setId(id);
        this.busRepository.save(bus);
    }



    private BusDTO convertToBusDTO(Bus bus){
        return this.busMapper.toDto(bus);
    }
}
