package tm.ugur.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class ImageDownload {

    private WebClient webClient;

//    public byte[] getImageByte(String url){
//        webClient = WebClient.create(url);
//        return  webClient.get()
//                .retrieve()
//                .bodyToMono(byte[].class)
//                .block();
//
//    }

    public byte[] getImageByte(String url){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, byte[].class).getBody();

    }
}
