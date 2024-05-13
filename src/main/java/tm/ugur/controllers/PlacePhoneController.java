package tm.ugur.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<?> store(@ModelAttribute("phone") @Valid PlacePhoneDTO phone, BindingResult result) {

        if(result.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField() + "_" + phone.getType(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        if(phone.getType().equals("city"))
            placePhoneService.addCity(phone);
        if(phone.getType().equals("mob")){
            placePhoneService.addMob(phone);
        }


        return ResponseEntity.ok(phone);
    }

    @PostMapping("/city")
    public ResponseEntity<?> deleteCity(){
        placePhoneService.deleteCity();
        return ResponseEntity.ok("Удален городской телефон.");
    }

    @PostMapping("/mob")
    public ResponseEntity<?> deleteMob(){
        placePhoneService.deleteMob();
        return ResponseEntity.ok("Удален мобильный телефон.");
    }
}
