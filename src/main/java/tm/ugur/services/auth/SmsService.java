package tm.ugur.services.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.Serializable;


@Service
public class SmsService {


    @Value("${sms_url}")
    private String urlBase;

    @Value("${sms_text}")
    private String text;



    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public SmsService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }


    public void sendSms(String phone, String otp) throws WebClientResponseException {
        String recipient = "+993" + phone;
        String content = this.text + otp;

//        WebClient.create(this.urlBase)
//                .post()
//                .body(BodyInserters.fromValue(new SmsRequest(recipient, content)))
//                .retrieve()
//                .toBodilessEntity()
//                .subscribe();
        SmsRequest smsRequest = new SmsRequest(recipient, content);
        rabbitTemplate.setExchange("direct-exchange");
        rabbitTemplate.convertAndSend("smsQueue", smsRequest);
    }

    @RabbitListener(queues = "smsQueue")
    public void handleSmsRequest(SmsRequest smsRequest) {
        System.out.println("Received SMS request: " + smsRequest);
        WebClient.create(urlBase)
                .post()
                .body(BodyInserters.fromValue(smsRequest))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

}

record SmsRequest( @JsonProperty("recipient") String recipient,
                   @JsonProperty("content") String content) {
}
