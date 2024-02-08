package tm.ugur.services.auth;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tm.ugur.dto.ClientDTO;
import tm.ugur.models.Client;
import tm.ugur.services.ClientService;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import static java.lang.Thread.sleep;

@Service
public class ClientRegistrationService {

    private final ClientOtpService clientOtpService;
    private final ClientService clientService;
    private final SmsService smsService;
    private final ModelMapper mapper;

    @Value("${sms_timeout_for_user_in_seconds}")
    private Long endTime;

    private final static Logger loggr = LoggerFactory.getLogger(ClientRegistrationService.class);

    @Autowired
    public ClientRegistrationService(ClientOtpService clientOtpService, ClientService clientService, SmsService smsService, ModelMapper mapper) {
        this.clientOtpService = clientOtpService;
        this.clientService = clientService;
        this.smsService = smsService;
        this.mapper = mapper;
    }

    public void register(ClientDTO clientDTO) throws Exception{
        Optional<Client> existingClient = clientService.findClientByPhone(clientDTO.getPhone());

        if (existingClient.isPresent()) {
            long timeOut = Duration.between(existingClient.get().getUpdatedAt().toInstant(), LocalTime.now()).getSeconds();
            if (timeOut <= endTime) {
                throw new Exception("To many request");
            }
        }
        Client client = mapper.map(clientDTO, Client.class);
        String otp = clientOtpService.generateAndSaveOtp(client);

        try {
            smsService.sendSms(client.getPhone(), otp);
        } catch (Exception e) {
            loggr.error("Sms service: " + e.getMessage());
            throw new Exception("SMS service failed: " + e.getMessage());
        }

    }
}
