package tm.ugur.services.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.geo.LineStringDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.graph.GraphHopperService;
import tm.ugur.util.Distance;
import tm.ugur.util.hopper.RouteResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoutePlanningService {

    private final GraphHopperService graphHopperService;
    private final ObjectMapper mapper;
    private final StopService stopService;
    private final Distance distance;

    @Autowired
    public RoutePlanningService(GraphHopperService graphHopperService, ObjectMapper mapper,
                                StopService stopService,
                                Distance distance) {
        this.graphHopperService = graphHopperService;
        this.mapper = mapper;
        this.stopService = stopService;
        this.distance = distance;
    }

    public Map<Integer, Map<String, LineStringDTO>> planning(double pointALat, double pointALng, double pointBLat, double pointBLng) {
        List<Stop> stops = stopService.findAll();

        Map<Double, Stop> nearbyStopsA = findNearbyStops(stops, pointALat, pointALng);
        Map<Double, Stop> nearbyStopsB = findNearbyStops(stops, pointBLat, pointBLng);

        Set<Route> intersectingRoutes = findIntersectingRoutes(
                findStartRoutes(nearbyStopsA.values()), findEndRoutes(nearbyStopsB.values()));

        Map<Route, String> routeSide = determineWhichEndPointClosest(intersectingRoutes, pointBLat, pointBLng);

        nearbyStopsA = refreshNearbyStops(routeSide, pointALat, pointALng);
        nearbyStopsB = refreshNearbyStops(routeSide, pointBLat, pointBLng);

        Map<Route, Stop> routeAStops = findRouteStops(nearbyStopsA.values(), intersectingRoutes);
        Map<Route, Stop> routeBStops = findRouteStops(nearbyStopsB.values(), intersectingRoutes);


        return  processPlanning(
                routeAStops.keySet(), routeSide, routeAStops, routeBStops, pointALat, pointALng, pointBLat, pointBLng);
    }

    private Map<Route, String> determineWhichEndPointClosest(Set<Route> routes, double lat, double lng) {
        Map<Route, String> side = new HashMap<>();

        routes.forEach(route -> {

            List<Stop> stopList = route.getStartRouteStops().stream()
                    .sorted(Comparator.comparing(StartRouteStop::getIndex))
                    .map(StartRouteStop::getStop).toList();

            Stop firstStop = stopList.getFirst();
            Stop lastStop = stopList.getLast();

            Point first = firstStop.getLocation();
            Point last = lastStop.getLocation();

            double firstDist = distance.calculateRadiusMeter(first.getX(), first.getY(), lat, lng);
            double lastDist = distance.calculateRadiusMeter(last.getX(), last.getY(), lat, lng);

            if(firstDist < lastDist)
                side.put(route, "back");
            else
                side.put(route, "front");
        });

        return side;
    }

    private Map<Double, Stop> refreshNearbyStops(Map<Route, String> routes, double lat, double lng) {
        List<Stop> stops = new ArrayList<>();

        routes.keySet().forEach(route -> {
            if(routes.get(route).equals("front"))
                stops.addAll(route.getStartRouteStops().stream().map(StartRouteStop::getStop).toList());
            else
                stops.addAll(route.getEndRouteStops().stream().map(EndRouteStop::getStop).toList());

        });

        return findNearbyStops(stops, lat, lng);
    }

    private Map<Double, Stop> findNearbyStops(List<Stop> stops, double lat, double lng) {
        Map<Double, Stop> nearbyStop = new TreeMap<>(Comparator.reverseOrder());

        stops.forEach(stop -> {
            Point location = stop.getLocation();
            double dist = distance.calculateRadiusMeter(lat, lng, location.getX(), location.getY());
            if (dist <= 1000) nearbyStop.put(dist, stop);
        });

        return nearbyStop;
    }



    private Set<Route> findStartRoutes(Collection<Stop> nearbyStops) {
        Set<Route> routes = new HashSet<>();
        nearbyStops.forEach(stop -> {
            routes.addAll(stop.getStartRoutes());
        });
        return routes;
    }

    private Set<Route> findEndRoutes(Collection<Stop> nearbyStops) {
        Set<Route> routes = new HashSet<>();
        nearbyStops.forEach(stop -> {
            routes.addAll(stop.getEndRoutes());
        });
        return routes;
    }

    private Set<Route> findIntersectingRoutes(Set<Route> routeListA, Set<Route> routeListB) {
        Set<Route> intersection = new HashSet<>(routeListA);
        intersection.retainAll(routeListB);
        return intersection;
    }

    private Map<Route, Stop> findRouteStops(Collection<Stop> stops, Set<Route> intersection) {
        Map<Route, Stop> routeStops = new HashMap<>();
        stops.forEach(stop -> {
            stop.getStartRoutes().forEach(route -> {
                if (intersection.contains(route)) routeStops.put(route, stop);
            });
            stop.getEndRoutes().forEach(route -> {
                if (intersection.contains(route)) routeStops.put(route, stop);
            });
        });

        return routeStops;
    }

    private Map<Integer, Map<String, LineStringDTO>> processPlanning(Set<Route> routes,
                                 Map<Route, String> routeSide,
                                 Map<Route, Stop> routeAStops,
                                 Map<Route, Stop> routeBStops,
                                 double pointALat, double pointALng,
                                 double pointBLat, double pointBLng) {

        Map<Integer, List<PointDTO>> points =
                processCoordinates(routes, routeSide, routeAStops, routeBStops, pointALat, pointALng, pointBLat, pointBLng);

        Map<Integer, Map<String, LineStringDTO>> res = new HashMap<>();

        for(Map.Entry<Integer, List<PointDTO>> entry : points.entrySet()) {
            int routeNumber = entry.getKey();
            List<PointDTO> pointDTOList = entry.getValue();

           String responseFoot = graphHopperService.calculateFootRoute(
                   pointALat, pointALng, pointDTOList.get(1).getLat(), pointDTOList.get(1).getLng());
           String responseBus = graphHopperService.calculateBusRoute(pointDTOList.subList(1, pointDTOList.size()));

           RouteResponse routeFootResponse = readObject(responseFoot);
           RouteResponse routeBusResponse = readObject(responseBus);

           Map<String, LineStringDTO> route = new HashMap<>();
           List<PointDTO> pointFootLists = new ArrayList<>();
           List<PointDTO> pointBusLists = new ArrayList<>();

           routeFootResponse.getPaths().getFirst().getPoints().getCoordinates().forEach(coordinate -> {
                pointFootLists.add(new PointDTO(coordinate.getLast(), coordinate.getFirst()));
            });

           routeBusResponse.getPaths().getFirst().getPoints().getCoordinates().forEach(coordinate -> {
               pointBusLists.add(new PointDTO(coordinate.getLast(), coordinate.getFirst()));
           });

            route.put("walk", new LineStringDTO(pointFootLists));
            route.put("bus", new LineStringDTO(pointBusLists));
            res.put(routeNumber, route);
        }

        return res;
    }

    private RouteResponse readObject(String response) {
        try {
           return mapper.readValue(response, RouteResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Integer, List<PointDTO>> processCoordinates(Set<Route> routes,
                                 Map<Route, String> routeSide,
                                 Map<Route, Stop> routeAStops,
                                 Map<Route, Stop> routeBStops,
                                 double pointALat, double pointALng,
                                 double pointBLat, double pointBLng) {
        Map<Integer, List<PointDTO>> routePoits = new HashMap<>();

        routes.forEach(route -> {
            List<Stop> list;
            if (routeSide.get(route).equals("front")) {
                list = route.getStartRouteStops().stream()
                        .sorted(Comparator.comparing(StartRouteStop::getIndex))
                        .map(StartRouteStop::getStop)
                        .collect(Collectors.toList());
            } else {
                list = route.getEndRouteStops().stream()
                        .sorted(Comparator.comparing(EndRouteStop::getIndex))
                        .map(EndRouteStop::getStop)
                        .collect(Collectors.toList());
            }


            int start = list.indexOf(routeAStops.get(route));
            int end = list.indexOf(routeBStops.get(route));


           if(end > 0) {
               List<PointDTO> points = new ArrayList<>();
               points.add(new PointDTO(pointALat, pointALng));
               list.subList(start, end + 1).forEach(stop -> {
                   Point point = stop.getLocation();
                   points.add(new PointDTO(point.getX(), point.getY()));
               });
               points.add(new PointDTO(pointBLat, pointBLng));
               routePoits.put(route.getNumber(), points);
           }
        });

        return routePoits;
    }
}
