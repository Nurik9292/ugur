package tm.ugur.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Bus;
import tm.ugur.services.BusSservice;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;
import tm.ugur.ws.MobWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@EnableAsync
public class BusScheduling {

    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;
    private final BusSservice busSservice;


    private final static Logger logger = LoggerFactory.getLogger(BusScheduling.class);


    @Autowired
    public BusScheduling(ImdataService imdataService, AtLogisticService atLogisticService, BusSservice busSservice) {
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.busSservice = busSservice;
    }

    @Async
    @Scheduled(fixedDelay = 5000)
    public void scheduleFixedDelayTask(){
        try {
            Map<String, String> map = this.imdataService.getDataBus();
            JsonNode jsonNode = this.atLogisticService.getDataBus();

                for (JsonNode node : jsonNode.get("list")) {
                    if (map.containsKey(node.get("vehiclenumber").asText())) {
                        Bus bus = new Bus(
                                node.get("vehiclenumber").asText(),
                                Integer.parseInt(map.get(node.get("vehiclenumber").asText())),
                                node.get("status").get("speed").asText(),
                                node.get("imei").asText(),
                                node.get("status").get("dir").asText(),
                                node.get("status").get("lat").asText(),
                                node.get("status").get("lon").asText()
                        );

                        Optional<Bus> busUpdate = this.busSservice.findByCarNumber(node.get("vehiclenumber").asText());
                        if(busUpdate.isEmpty()) {
                            this.busSservice.store(bus);
                        }else {
                            this.busSservice.update(busUpdate.get().getId(), bus);
                        }
                    }
                }
        } catch (Exception e) {
            logger.error("API unavailable: " + e.getMessage());
            this.busSservice.deleteAll();
        }
    }
}