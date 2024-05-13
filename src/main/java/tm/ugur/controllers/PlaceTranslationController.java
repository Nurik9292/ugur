package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.dto.TranslationDTO;
import tm.ugur.services.admin.PlaceTranslationService;
import tm.ugur.services.redis.RedisPlaceTranslationService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/place-translations")
public class PlaceTranslationController {

    private final PlaceTranslationService translationService;
    private final RedisPlaceTranslationService redisTranslationService;

    @Autowired
    public PlaceTranslationController(PlaceTranslationService translationService,
                                      RedisPlaceTranslationService redisTranslationService) {
        this.translationService = translationService;
        this.redisTranslationService = redisTranslationService;
    }

    @PostMapping
    public ResponseEntity<?> store(@ModelAttribute("translation") @Valid TranslationDTO translation, BindingResult result) {

        if(result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField() + "_" + translation.getLocale(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        System.out.println(translation);

        if (translation.getLocale().equals("tm"))
            redisTranslationService.addTm(translation);
        if (translation.getLocale().equals("ru"))
            redisTranslationService.addRu(translation);
        if (translation.getLocale().equals("en"))
            redisTranslationService.addEn(translation);

        return ResponseEntity.ok(translation);
    }
}
