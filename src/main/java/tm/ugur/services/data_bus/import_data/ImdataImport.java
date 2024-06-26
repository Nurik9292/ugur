package tm.ugur.services.data_bus.import_data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.data_bus.BusResponseService;

import java.util.*;


@Service
public class ImdataImport extends BusDataImplImport {

    private final BusResponseService response;
    private StringBuilder builder;

    private final static Logger logger = LoggerFactory.getLogger(ImdataImport.class);

    @Autowired
    public ImdataImport(BusResponseService response) {
        this.response = response;
        this.builder = new StringBuilder();
    }

    @Override
    public Map<String, BusDTO> getBusData() {
        try {
            JsonNode json = response.getData("https://edu.ayauk.gov.tm/gps/buses/info",
                    "turkmenportal", "turkmenportal");
            Map<String, BusDTO> buses = new TreeMap<>();
            json.forEach(object -> {
                String carNumber = stringProcessing(object.get("car_number").asText());
                  buses.put(carNumber, new BusDTO(
                          carNumber,
                          Integer.parseInt(object.get("number").asText())));
            });
            return buses;
        }catch (JsonProcessingException e){
            logger.error("Api imdata unavailable: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private String stringProcessing(String carNumber){
        builder.setLength(0);
        builder.append(carNumber);
        builder.insert(2, "-");
        if(builder.indexOf(" ") != 5)
            builder.insert(5, " ");
        return builder.toString();
    }
}
