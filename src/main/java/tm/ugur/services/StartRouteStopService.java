package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.repo.StartRouteStopRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class StartRouteStopService {

    private static int count = 1;

    private final StartRouteStopRepository startRouteStopRepository;


    @Autowired
    public StartRouteStopService(StartRouteStopRepository startRouteStopRepository) {
        this.startRouteStopRepository = startRouteStopRepository;
    }

    @Transactional
    public void updateIndexs(String ids, Route route){
        int count = 1;
        List<Stop> stops = route.getStartStops();
        System.out.println(stops);
        for (String id : ids.split(",")){
            System.out.println(id);
            for(Stop stop : stops){
                if(stop.getId() == Integer.parseInt(id)){
                    List<StartRouteStop> startRouteStops = this.startRouteStopRepository.findByStop(stop);
                    for(StartRouteStop startRouteStop : startRouteStops){
                            if(startRouteStop.getRoute().getId() == route.getId()){
                                startRouteStop.setIndex(count++);
                                this.startRouteStopRepository.save(startRouteStop);
                            }
                        }

                }
            }
        }

    }
}
