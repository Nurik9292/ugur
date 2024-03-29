package tm.ugur.services.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


@Service
public class ParserService {

    public List<PlaceUgur> parser(){

        ClassPathResource resource = new ClassPathResource("places.json");
        try(InputStream fis = resource.getInputStream()) {
            byte[] jsonData = fis.readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            List<PlaceUgur> places = new ArrayList<>();
            objectMapper.readTree(jsonData).get("places").forEach(node -> {
                List<Category> categories = new ArrayList<>();
                node.get("categories").forEach(category -> {
                    categories.add(new Category(
                            category.get("name_tm").asText(),
                            category.get("name_ru").asText()
                    ));
                });

               places.add(new PlaceUgur(
                       node.get("name_tm").asText(),
                       node.get("name_ru").asText(),
                       "https://yakyn.biz:8000/media/businesses/l/" + node.get("image").asText(),
                       node.get("address_tm").asText(),
                       node.get("address_ru").asText(),
                       Double.parseDouble(node.get("latitude").asText()),
                       Double.parseDouble(node.get("longitude").asText()),
                       categories
               ));
            });
            System.out.println(places);
            return places;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
