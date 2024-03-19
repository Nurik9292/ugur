package tm.ugur.controllers.api.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.ClientDTO;
import tm.ugur.dto.auth.AuthenticationClientDTO;
import tm.ugur.models.Client;
import tm.ugur.security.JWTUtil;
import tm.ugur.services.admin.ClientService;
import tm.ugur.services.auth.ClientRegistrationService;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthClientController {

    private final JWTUtil jwtUtil;
    private final ClientService clientService;
    private final ClientRegistrationService clientRegistrationService;


    @Autowired
    public AuthClientController(JWTUtil jwtUtil, ClientService clientService, ClientRegistrationService clientRegistrationService) {
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
        this.clientRegistrationService = clientRegistrationService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> cleintRegister(@RequestBody @Valid ClientDTO clientDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ошибка"));
        }

        try {
            this.clientRegistrationService.register(clientDTO);

            return ResponseEntity.ok(Map.of("message", "Sms send"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<Map<String, String>> verifyOpt(@RequestBody AuthenticationClientDTO authenticationDTO,
                                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Errors"));
        }


        Optional<Client> optionalClient = clientService.findClientByPhone(authenticationDTO.getPhone());

        if(optionalClient.isPresent()) {
            if (!optionalClient.get().getOtp().equals(authenticationDTO.getOtp())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Unauthorized connection attempt"));
            }

            this.clientService.update(optionalClient.get(), authenticationDTO);

            return ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(authenticationDTO.getPhone())));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Not found client"));
    }


    @ExceptionHandler
    public ResponseEntity<String>  errors(Exception exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
