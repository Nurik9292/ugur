package tm.ugur.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.dto.RouteDTO;
import tm.ugur.services.RouteService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class RouteSeed implements CommandLineRunner {

    private final RouteService routeService;

    @Autowired
    public RouteSeed(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(this.routeService.findAll().isEmpty()) {
            ClassPathResource resource = new ClassPathResource("routes.json");
            try (FileInputStream fis = new FileInputStream(resource.getFile())) {
                ObjectMapper mapper = new ObjectMapper();
                List<RouteDTO> routes = mapper.readValue(fis, new TypeReference<List<RouteDTO>>() {
                });

                routes.forEach(this.routeService::save);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
