package tm.ugur.services.data_bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.EndRouteStopService;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.admin.StartRouteStopService;
import tm.ugur.services.admin.StopService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Predicate;


@Component
public class BusIndexing {

    private final StopService stopService;
    private final RouteService routeService;
    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;
    private final Lock lock;

    private final static Logger logger = LoggerFactory.getLogger(BusIndexing.class);

    @Autowired
    public BusIndexing(StopService stopService,
                       RouteService routeService,
                       StartRouteStopService startRouteStopService,
                       EndRouteStopService endRouteStopService, Lock lock) {
        this.stopService = stopService;
        this.routeService = routeService;
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
        this.lock = lock;
    }


    @Transactional
    public List<BusDTO> indexing(List<BusDTO> buses) {

        buses.parallelStream().forEach(bus -> {
            PointDTO point = bus.getLocation();

            List<Stop> stops = stopService.findNearestStops(point.getLat(), point.getLng());
            Optional<Route> route = routeService.findByNumber(bus.getNumber());

            if (Objects.isNull(bus.getSide())) {
                return;
            }

            if (bus.getSide().equals("front")) {
                processStopsForSide(stops, route, bus, this::processStartRouteStop);
            } else if (bus.getSide().equals("back")) {
                processStopsForSide(stops, route, bus, this::processEndRouteStop);
            }
        });


        return buses;
    }

    private void processStopsForSide(List<Stop> stops, Optional<Route> route, BusDTO bus,
                                     BiConsumer<Optional<?>, BusDTO> routeStopProcessor) {
        if (route.isPresent()) {
            stops.parallelStream().forEach(stop -> {
                Optional<?> routeStop = bus.getSide().equals("front")
                        ? startRouteStopService.findByStopAndRoute(stop, route.get())
                        : endRouteStopService.findByStopAdnRoute(stop, route.get());

                routeStopProcessor.accept(routeStop, bus);
            });
        }
    }

    private void processStartRouteStop(Optional<?> routeStop, BusDTO bus) {
        processRouteStop(routeStop, bus, index -> index % 2 == 0);
    }

    private void processEndRouteStop(Optional<?> routeStop, BusDTO bus) {
        processRouteStop(routeStop, bus, index -> index % 2 != 0);
    }

    private void processRouteStop(Optional<?> routeStop, BusDTO bus, Predicate<Integer> indexCondition) {
        routeStop.ifPresent(stop -> {
            int index = stop instanceof StartRouteStop ?
                    ((StartRouteStop) stop).getIndex() : ((EndRouteStop) stop).getIndex();
            if (indexCondition.test(index)) {
                bus.setIndex(index);
            }
        });
    }





}
