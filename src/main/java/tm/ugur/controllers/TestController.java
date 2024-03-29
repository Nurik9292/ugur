package tm.ugur.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import tm.ugur.models.PlaceCategory;
import tm.ugur.models.PlaceSubCategory;
import tm.ugur.models.PlaceSubCategoryTranslation;
import tm.ugur.services.admin.PlaceService;
import tm.ugur.services.admin.PlaceSubCategoryTranslationService;
import tm.ugur.services.api.ApiService;
import tm.ugur.services.parser.PlaceUgur;
import tm.ugur.services.parser.ParserService;

import java.awt.*;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Controller
public class TestController {

    private final ParserService parserService;
    private final PlaceService placeService;
    private final PlaceSubCategoryTranslationService translationService;
    private final ApiService apiService;

    @Autowired
    public TestController(ParserService parserService,
                          PlaceService placeService,
                          PlaceSubCategoryTranslationService translationService, ApiService apiService) {
        this.parserService = parserService;
        this.placeService = placeService;
        this.translationService = translationService;
        this.apiService = apiService;
    }

    @GetMapping("/test")
    public String test(){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(apiService.fetchDataFromApi());
            System.out.println(jsonNode.get("banners"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return "null";
    }
}
