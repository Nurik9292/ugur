package tm.ugur.services.data_bus;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;


@Service
public class ImdataService implements ImportBusData {

    private final BusResponseService response;
    private Map<String, String> uniqueCarNumber;
    private StringBuilder builder;

    @Autowired
    public ImdataService(BusResponseService response) {
        this.response = response;
        this.builder = new StringBuilder();
    }

    public Map<String, String> getDataBus(){
        JsonNode json = this.response.getData("https://edu.ayauk.gov.tm/gps/buses/info",
                "turkmenportal", "turkmenportal");

        this.uniqueCarNumber = new TreeMap<>();

        json.forEach( obj -> {
            this.uniqueCarNumber.put(this.split(obj.get("car_number").asText()), obj.get("number").asText());
        });

        return this.uniqueCarNumber;
    }


    private String split(String carNumber){
        this.builder.setLength(0);
        this.builder.append(carNumber);
        this.builder.insert(2, "-");

        return this.builder.toString();
    }
}
