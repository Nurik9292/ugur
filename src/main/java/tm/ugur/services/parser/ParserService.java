package tm.ugur.services.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tm.ugur.services.api.ApiService;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Service
public class ParserService {

    private final ApiService apiService;

    @Autowired
    public ParserService(ApiService apiService) {
        this.apiService = apiService;
    }

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

            return places;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<PlaceUgur> parserUrl(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(apiService.fetchDataFromApi());
            List<PlaceUgur> places = new ArrayList<>();
            for(JsonNode node : jsonNode.get("data").get("banners")){
            
                if(node.get("businesses") != null && !node.get("businesses").isEmpty()){
                    node.get("businesses").forEach(place -> {

                       
                        List<Category> categories = new ArrayList<>();
                        place.get("categories").forEach(category -> {
                            categories.add(new Category(
                                    category.get("name_tm").asText(),
                                    category.get("name_ru").asText()
                            ));
                        });

                        if(!place.get("latitude").isNull())
                            places.add(new PlaceUgur(
                                place.get("name_tm").asText(),
                                place.get("name_ru").asText(),
                                "https://yakyn.biz:8000/media/businesses/l/" + place.get("image").asText(),
                                place.get("address_tm").asText(),
                                place.get("address_ru").asText(),
                                Double.parseDouble(place.get("latitude").asText()),
                                Double.parseDouble(place.get("longitude").asText()),
                                categories
                            ));
                    });
                }
            }

            return places;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
