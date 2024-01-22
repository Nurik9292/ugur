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

@Service
@EnableAsync
public class BusScheduling {

    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;
    private final MobWebSocketHandler mobWebSocketHandler;
    private final BusSservice busSservice;

    private boolean isApiAvailable = true;

    private final static Logger logger = LoggerFactory.getLogger(BusScheduling.class);


    @Autowired
    public BusScheduling(ImdataService imdataService, AtLogisticService atLogisticService, MobWebSocketHandler mobWebSocketHandler, BusSservice busSservice) {
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.mobWebSocketHandler = mobWebSocketHandler;
        this.busSservice = busSservice;
    }

    @Async
    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask(){
        try {
            Map<String, String> map = this.imdataService.getDataBus();
            JsonNode jsonNode = this.atLogisticService.getDataBus();
            List<BusDTO> buses = new ArrayList<>();

            if (isApiAvailable) {
                for (JsonNode node : jsonNode.get("list")) {
                    if (map.containsKey(node.get("vehiclenumber").asText())) {
//                        BusDTO busDTO = new BusDTO(
//                                node.get("vehiclenumber").asText(),
//                                map.get(node.get("vehiclenumber").asText()),
//                                node.get("status").get("speed").asText(),
//                                node.get("imei").asText(),
//                                node.get("status").get("dir").asText(),
//                                node.get("status").get("lat").asText(),
//                                node.get("status").get("lon").asText()
//                        );

                        Bus bus = new Bus(
                                node.get("vehiclenumber").asText(),
                                map.get(node.get("vehiclenumber").asText()),
                                node.get("status").get("speed").asText(),
                                node.get("imei").asText(),
                                node.get("status").get("dir").asText(),
                                node.get("status").get("lat").asText(),
                                node.get("status").get("lon").asText()
                        );

                        Bus busUpdate = this.busSservice.findByCarNumber(node.get("vehiclenumber").asText());
//                        buses.add(busDTO);

                        if(busUpdate != null)
                            this.busSservice.store(bus);
                        else
                            this.busSservice.update(busUpdate.getId(), bus);
                    }
                }
//                ObjectMapper mapper = new ObjectMapper();
//                this.mobWebSocketHandler.sendToMobileApp(mapper.writeValueAsString(buses));
            }
        } catch (Exception e) {
            isApiAvailable = false;
            logger.error("API unavailable: " + e.getMessage());

            mobWebSocketHandler.sendToMobileApp("API temporarily unavailable");
        }
    }
}