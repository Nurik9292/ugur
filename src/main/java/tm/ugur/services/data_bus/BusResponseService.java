package tm.ugur.services.data_bus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tm.ugur.job.BusScheduling;

import java.util.Collections;

@Service
public class BusResponseService {

    private final static Logger logger = LoggerFactory.getLogger(BusScheduling.class);

    public JsonNode getData(String url, String user, String password){
        try {
            ResponseEntity<String> response = this.connection(url, user, password);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            logger.error("Api json parsing: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<String> connection(String url, String user, String password){
        BasicAuthenticationInterceptor interceptor =
                new BasicAuthenticationInterceptor(user, password);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(interceptor));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}
