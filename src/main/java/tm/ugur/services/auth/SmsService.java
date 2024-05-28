package tm.ugur.services.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
public class SmsService {


    @Value("${sms_url}")
    private String urlBase;

    @Value("${sms_text}")
    private String text;

    private final RabbitTemplate rabbitTemplate;

    private final static Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    public SmsService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }


//    public void sendSms(String phone, String otp) throws WebClientResponseException {
//        String recipient = "+993" + phone;
//        String content = this.text + otp;
//
//        SmsRequest smsRequest = new SmsRequest(recipient, content);
//        rabbitTemplate.setExchange("direct-exchange");
//        rabbitTemplate.convertAndSend("smsQueue", smsRequest);
//    }
//
//    @RabbitListener(queues = "smsQueue")
//    public void handleSmsRequest(SmsRequest smsRequest) {
//        logger.info("Received SMS request: " + smsRequest);
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<SmsRequest> requestEntity = new HttpEntity<>(smsRequest, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(urlBase, requestEntity, String.class);
//        logger.info(String.valueOf(response));
//    }

    public void sendSms(String phone, String otp) throws WebClientResponseException {
        String recipient = "+993" + phone;
        String content = this.text + otp;

        WebClient.create(this.urlBase)
                .post()
                .body(BodyInserters.fromValue(new SmsRequest(recipient, content)))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }


}

record SmsRequest  (String recipient, String content) {
    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }
}

