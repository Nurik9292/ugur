package tm.ugur.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.dto.PlacePhoneDTO;
import tm.ugur.services.redis.RedisPlacePhoneService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/place-phones")
public class PlacePhoneController {


    private final RedisPlacePhoneService placePhoneService;

    @Autowired
    public PlacePhoneController(RedisPlacePhoneService placePhoneService) {
        this.placePhoneService = placePhoneService;
    }

    @PostMapping
    public ResponseEntity<?> store(@ModelAttribute("phone") PlacePhoneDTO phone, BindingResult result) {

        System.out.println(phone);

        if(result.hasErrors()){;
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }


        if(phone.getType().equals("city"))
            placePhoneService.addCity(phone);
        if(phone.getType().equals("mob")){
            placePhoneService.delete();
            placePhoneService.addMob(phone);
        }

        return ResponseEntity.ok(phone);
    }
}
