package tm.ugur.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ImageDownload {

    private WebClient webClient;

    public byte[] getImageByte(String url){
        webClient = WebClient.create(url);
        return  webClient.get()
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

    }
}
