package tm.ugur.services.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class SmsService {


    @Value("${sms_url}")
    private String urlBase;

    @Value("${sms_text}")
    private String text;



    public void sendSms(String phone, String otp){
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

record SmsRequest(String recipient, String content) {
}
