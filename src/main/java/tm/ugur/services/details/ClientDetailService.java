package tm.ugur.services.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tm.ugur.models.Client;
import tm.ugur.repo.ClientRepository;
import tm.ugur.security.ClientDetails;

import java.util.Optional;

@Service
public class ClientDetailService implements UserDetailsService {


    private final ClientRepository clientRepository;

    @Autowired
    public ClientDetailService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userPhone) throws UsernameNotFoundException {

        Optional<Client> client = this.clientRepository.findClientByPhone(userPhone);

        return client.map(ClientDetails::new).orElseThrow(() -> new UsernameNotFoundException("Нет такого пользователя"));
    }
}
