package tm.ugur.storage;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {

    @Value("${upload.image}")
    private String location;

    public String getLocation() {
        return location;
    }

}
