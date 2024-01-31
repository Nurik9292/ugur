package tm.ugur.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tm.ugur.dto.StopDTO;
import tm.ugur.services.StopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class StopSeed implements CommandLineRunner {

    private final StopService stopService;

    @Autowired
    public StopSeed(StopService stopService) {
        this.stopService = stopService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.stopService.findStopByName("Telekeçiler merkezi (Gündogar tarap)").isEmpty()) {
            try (FileInputStream fis = new FileInputStream(new File("stop.json"))) {
                ObjectMapper mapper = new ObjectMapper();
                List<StopDTO> stops = mapper.readValue(fis, new TypeReference<List<StopDTO>>() {});
                stops.forEach(this.stopService::save);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
