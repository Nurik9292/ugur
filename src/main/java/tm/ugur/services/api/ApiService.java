package tm.ugur.services.api;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    public String fetchDataFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://yakyn.biz:8000/api/home";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}
