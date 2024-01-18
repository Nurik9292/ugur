package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.repo.EndRouteStopRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EndRouteStopService {

    private static int count = 1;

    private final EndRouteStopRepository endRouteStopRepository;

    @Autowired
    public EndRouteStopService(EndRouteStopRepository endRouteStopRepository) {
        this.endRouteStopRepository = endRouteStopRepository;
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
                    List<EndRouteStop> endRouteStops = this.endRouteStopRepository.findByStop(stop);
                    for(EndRouteStop endRouteStop : endRouteStops){
                        if(endRouteStop.getRoute().getId() == route.getId()){
                            endRouteStop.setIndex(count++);
                            this.endRouteStopRepository.save(endRouteStop);
                        }
                    }

                }
            }
        }

    }
}
