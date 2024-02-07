package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.auth.AuthenticationClientDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.repo.ClientRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAll(){
        return this.clientRepository.findAll();
    }

    public Client FindOne(Long id){
        return this.clientRepository.findById(id).orElse(null);
    }

    public Optional<Client> findClientByPhone(String phoneNumber){
        return this.clientRepository.findClientByPhone(phoneNumber);
    }

    @Transactional
    public void store(Client client){
        client.setCreatedAt(new Date());
        client.setUpdatedAt(new Date());
        this.clientRepository.save(client);
    }

    @Transactional
    public void update(Client client, AuthenticationClientDTO authenticationClientDTO){
        client.setUpdatedAt(new Date());
        client.setOtpVerify(true);
        client.setPlatform(authenticationClientDTO.getPlatform());
        this.clientRepository.save(client);
    }

    @Transactional
    public void update(long id, Client client){
        client.setId(id);
        client.setUpdatedAt(new Date());
        this.clientRepository.save(client);
    }

    @Transactional
    public void delete(long id){
        this.clientRepository.deleteById(id);
    }


    public Optional<Client> findClientByRouteAndId(Route route, Long id){
        return this.clientRepository.findClientByRoutesAndId(route, id);
    }
}
