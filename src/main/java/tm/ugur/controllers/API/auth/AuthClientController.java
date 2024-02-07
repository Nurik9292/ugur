package tm.ugur.controllers.API.auth;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.ClientDTO;
import tm.ugur.dto.auth.AuthenticationClientDTO;
import tm.ugur.models.Client;
import tm.ugur.security.JWTUtil;
import tm.ugur.services.ClientService;
import tm.ugur.services.auth.ClientAuthService;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthClientController {

    private final JWTUtil jwtUtil;
    private final ClientService clientService;
    private final ClientAuthService clientAuthService;


    @Autowired
    public AuthClientController(JWTUtil jwtUtil, ClientService clientService, ClientAuthService clientAuthService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
        this.clientAuthService = clientAuthService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> cleintRegister(@RequestBody @Valid ClientDTO clientDTO, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ошибка"));
        }
        Map<String, String> response = this.clientAuthService.registration(clientDTO);

        if (response.containsKey("send")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Map.of("message", response.get("toManyRequest")));
        }
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<Map<String, String>> verifyOpt(@RequestBody AuthenticationClientDTO authenticationDTO,
                                                         BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Ошибка"));
        }

        Optional<Client> optionalClient = clientService.findClientByPhone(authenticationDTO.getPhone());

        if(optionalClient.isPresent()) {

            if (!optionalClient.get().getOtp().equals(authenticationDTO.getOtp())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            this.clientService.update(optionalClient.get(), authenticationDTO);

            return ResponseEntity.ok(Map.of("jwt-token", jwtUtil.generateToken(authenticationDTO.getPhone())));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
