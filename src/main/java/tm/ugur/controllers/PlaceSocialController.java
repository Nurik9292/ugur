package tm.ugur.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.dto.SocialNetworkDTO;
import tm.ugur.services.redis.RedisPlaceSocialService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/place-socials")
public class PlaceSocialController {

    private final RedisPlaceSocialService socialService;

    @Autowired
    public PlaceSocialController(RedisPlaceSocialService socialService) {
        this.socialService = socialService;
    }

    @PostMapping
    public ResponseEntity<?> store(@ModelAttribute("social") @Valid SocialNetworkDTO social, BindingResult result) {

        System.out.println(social);

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        if(social.getName().equals("instagram"))
            socialService.addInstagram(social);
        if(social.getName().equals("tiktok"))
            socialService.addTikTok(social);

        return ResponseEntity.ok("успешно добавленно");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete() {
        socialService.delete();
        return ResponseEntity.ok(" Успешно удаленно");
    }

}
