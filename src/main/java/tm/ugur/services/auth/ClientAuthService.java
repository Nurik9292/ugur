package tm.ugur.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.repo.ClientRepository;

@Service
public class ClientAuthService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientAuthService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


}
