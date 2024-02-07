package tm.ugur.controllers.API.auth;

    import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.ClientDTO;
import tm.ugur.models.Client;
import tm.ugur.security.JWTUtil;
import tm.ugur.services.ClientService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ModelMapper mapper;
    private final JWTUtil jwtUtil;
    private final ClientService clientService;

    @Autowired
    public AuthController(ModelMapper mapper, JWTUtil jwtUtil, ClientService clientService) {
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
        this.clientService = clientService;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>>  clientRegistration(@RequestBody @Valid ClientDTO clientDTO, BindingResult result){

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(Map.of("message", "Ошибка"));
        }
        Client client = this.mapper.map(clientDTO, Client.class);
        this.clientService.store(client);

        String token = this.jwtUtil.generateToken(client.getPhone());

        return ResponseEntity.ok(Map.of("token", token)) ;
    }
}
