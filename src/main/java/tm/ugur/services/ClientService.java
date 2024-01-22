package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Client;
import tm.ugur.repo.ClientRepository;

import java.util.List;

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

    public Client getOne(long id){
        return this.clientRepository.findById(id).orElse(null);
    }

    @Transactional
    public void store(Client client){
        this.clientRepository.save(client);
    }

    @Transactional
    public void update(long id, Client client){
        client.setId(id);
        this.clientRepository.save(client);
    }

    @Transactional
    public void delete(long id){
        this.clientRepository.deleteById(id);
    }
}
