package tm.ugur.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ImageDownload {

    public byte[] getImageByte(String url){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, byte[].class).getBody();

    }
}
