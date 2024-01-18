package tm.ugur.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;
import tm.ugur.ws.MobWebSocketHandler;

@Service
@EnableAsync
public class BusScheduling {

    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;
    private final MobWebSocketHandler mobWebSocketHandler;

    @Autowired
    public BusScheduling(ImdataService imdataService, AtLogisticService atLogisticService, MobWebSocketHandler mobWebSocketHandler) {
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.mobWebSocketHandler = mobWebSocketHandler;
    }

//    @Async
//    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelayTask() throws JsonProcessingException {
//        Map<String, String> map = this.imdataService.getDataBus();
//        List<BusDTO> buses = new ArrayList<>();
//
//        JsonNode jsonNode = this.atLogisticService.getDataBus();
//
//        for (JsonNode node : jsonNode.get("list")) {
//            if (map.containsKey(node.get("vehiclenumber").asText())) {
//                BusDTO bus = new BusDTO(
//                        node.get("vehiclenumber").asText(),
//                        map.get(node.get("vehiclenumber").asText()),
//                        node.get("status").get("speed").asText(),
//                        node.get("imei").asText(),
//                        node.get("status").get("dir").asText(),
//                        node.get("status").get("lat").asText(),
//                        node.get("status").get("lon").asText()
//                );
//
//                ObjectMapper mapper = new ObjectMapper();
//
//                this.mobWebSocketHandler.sendToMobileApp(mapper.writeValueAsString(bus));
//            }
//        }
//    }
}