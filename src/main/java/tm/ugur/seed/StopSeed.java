package tm.ugur.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.services.admin.StopService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
@Order(3)
public class StopSeed implements CommandLineRunner {

    private final StopService stopService;

    @Autowired
    public StopSeed(StopService stopService) {
        this.stopService = stopService;
    }

    @Override
    @DependsOn("CitySeed")
    public void run(String... args) throws Exception {
        if (!this.stopService.hasRouteByName("105-nji ýangyç bekedi (G.Kulyýew köçesi, ugur Günbatara tarap)")) {
            ClassPathResource resource = new ClassPathResource("stops.json");
            try (FileInputStream fis = new FileInputStream(resource.getFile())) {
                ObjectMapper mapper = new ObjectMapper();
                List<StopDTO> stops = mapper.readValue(fis, new TypeReference<List<StopDTO>>() {});
                stops.forEach(this.stopService::save);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
