package tm.ugur.services.graph;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tm.ugur.dto.geo.PointDTO;

import java.util.List;

@Service
public class GraphHopperService {

    private final RestTemplate restTemplate;

    @Value("${hopper.host}")
    private String hostRouteApi;

    @Autowired
    public GraphHopperService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String calculateFootRoute(double fromLat, double fromLon, double toLat, double toLon) {

        String url = UriComponentsBuilder.fromHttpUrl(hostRouteApi)
                .queryParam("point", fromLat + "," + fromLon)
                .queryParam("point", toLat + "," + toLon)
                .queryParam("profile", "foot")
                .queryParam("points_encoded", false)
                .queryParam("calc_points", true)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return responseEntity.getBody();
    }

    public String calculateBusRoute(List<PointDTO> points) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(hostRouteApi)
                .queryParam("profile", "foot")
                .queryParam("points_encoded", false)
                .queryParam("calc_points", true);

        points.forEach(point -> {
            uriBuilder.queryParam("point", point.getLat() + "," + point.getLng());
        });


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return responseEntity.getBody();
    }
}
