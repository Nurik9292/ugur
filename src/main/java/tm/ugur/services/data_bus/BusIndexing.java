package tm.ugur.services.data_bus;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Component
public class BusIndexing {

    private final GeometryFactory factory;
    private final RouteService routeService;
    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;
    private final Lock lock;

    private final static Logger logger = LoggerFactory.getLogger(BusIndexing.class);

    @Autowired
    public BusIndexing(GeometryFactory factory,
                       RouteService routeService,
                       StartRouteStopService startRouteStopService,
                       EndRouteStopService endRouteStopService, Lock lock) {
        this.factory = factory;
        this.routeService = routeService;
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
        this.lock = lock;
    }


    @Transactional
    public List<BusDTO> indexing(List<BusDTO> buses) {

        buses.parallelStream().forEach(bus -> {
            PointDTO point = bus.getLocation();

            Optional<Route> route = routeService.findByNumberInitStops(bus.getNumber());


            List<Stop> stops = Objects.nonNull(bus.getSide()) && bus.getSide().equals("front") ?
                    findNearestStops(route.get().getStartStops(), point) : findNearestStops(route.get().getEndStops(), point) ;


            if (Objects.isNull(bus.getSide())) {
                return;
            }

            if (Objects.nonNull(bus.getSide()) && bus.getSide().equals("front")) {
                processStopsForSide(stops, route, bus, this::processStartRouteStop);
            } else if (bus.getSide().equals("back")) {
                processStopsForSide(stops, route, bus, this::processEndRouteStop);
            }
        });


        return buses;
    }

    private List<Stop> findNearestStops(List<Stop> stops, PointDTO point){
        Point pointTarget = factory.createPoint(new Coordinate(point.getLat(), point.getLng()));
        return stops.parallelStream()
                .map(stop -> new AbstractMap.SimpleEntry<>(stop, stop.getLocation().distance(pointTarget)))
                .sorted(Comparator.comparingDouble(AbstractMap.SimpleEntry::getValue))
                .limit(1)
                .map(AbstractMap.SimpleEntry::getKey)
                .collect(Collectors.toList());
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
