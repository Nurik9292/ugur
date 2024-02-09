package tm.ugur.services.data_bus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;

import java.util.*;

@Service
public class AtLogisticImport extends BusDataImplImport {

    private final BusResponseService responseService;

    private final static Logger logger = LoggerFactory.getLogger(AtLogisticImport.class);

    @Autowired
    public AtLogisticImport(BusResponseService responseService) {
        this.responseService = responseService;
    }

    @Override
    public Map<String, BusDTO> getBusData() {
        try {
            JsonNode json = responseService.getData("https://atlogistika.com/api/api.php?cmd=list",
                    "turkmenavtoulag", "Awto996");

            Map<String, BusDTO> buses = new TreeMap<>();

            json.get("list").forEach(node -> {
                buses.put(!node.get("vehiclenumber").asText().isBlank() ? node.get("vehiclenumber").asText().trim() : "",
                        new BusDTO(
                        node.get("status").get("speed").asText(),
                        node.get("status").get("dir").asText(),
                        node.get("status").get("lat").asText(),
                        node.get("status").get("lon").asText()
                ));
            });

            return buses;
        } catch (JsonProcessingException e) {
            logger.error("Api atlogistika unavailable: " + e.getMessage());
            return Collections.EMPTY_MAP;
        }
    }
}
