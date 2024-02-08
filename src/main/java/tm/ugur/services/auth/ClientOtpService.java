package tm.ugur.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.models.Client;
import tm.ugur.services.ClientService;

import java.util.Random;
import java.util.stream.IntStream;

@Service
public class ClientOtpService {

    private final ClientService clientService;


    @Autowired
    public ClientOtpService(ClientService clientService) {
        this.clientService = clientService;
    }

    public String generateAndSaveOtp(Client client) {
        Random random = new Random();
        String otp = IntStream.range(1000, 10000).boxed().skip(random.nextInt(9000)).findFirst().get().toString();
        client.setOtp(otp);
        clientService.store(client);
        return otp;
    }
}
