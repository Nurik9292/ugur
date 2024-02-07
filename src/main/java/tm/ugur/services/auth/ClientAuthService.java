package tm.ugur.services.auth;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tm.ugur.dto.ClientDTO;
import tm.ugur.models.Client;
import tm.ugur.repo.ClientRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class ClientAuthService {

    private final ClientRepository clientRepository;
    private final SmsService smsService;
    private final ModelMapper mapper;

    @Value("${sms_timeout_for_user_in_seconds}")
    private Long endTime;

    @Autowired
    public ClientAuthService(ClientRepository clientRepository, SmsService smsService, ModelMapper mapper) {
        this.clientRepository = clientRepository;
        this.smsService = smsService;
        this.mapper = mapper;
    }

    public Map<String, String> registration(ClientDTO clientDTO){
        Optional<Client> optionalClient = this.clientRepository.findClientByPhone(clientDTO.getPhone());
        Client client = null;
        long timeOut = 0;
        Random random = new Random();

        if(optionalClient.isPresent()){
            client = optionalClient.get();
            timeOut = Duration.between(client.getUpdatedAt().toInstant(), LocalTime.now()).getSeconds();

            if (timeOut <= this.endTime) {
                return Map.of("toManyRequest",  "Попробуйте через " + (this.endTime - timeOut) + " секудну");
            }
        }else{
             client = this.mapper.map(clientDTO, Client.class);
        }

        client.setOtp(
                IntStream.range(1000, 10000).boxed().skip(random.nextInt(9000)).findFirst().get().toString());


        this.clientRepository.save(client);
        smsService.sendSms(client.getPhone(), client.getOtp());

        return Map.of("send", "Sms send");
    }

}
