package tm.ugur.services.data_bus;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;


@Service
public class ImdataService implements ImportBusData {

    private final BusResponseService response;

    @Autowired
    public ImdataService(BusResponseService response) {
        this.response = response;
    }

    public Map<String, String> getDataBus(){
        String url = "https://edu.ayauk.gov.tm/gps/buses/info";
        JsonNode json = this.response.getData(url, "turkmenportal", "turkmenportal");

        Map<String, String> uniqueCarNumber = new TreeMap<>();

        json.forEach( obj -> {
            uniqueCarNumber.put(this.split(obj.get("car_number").asText()), obj.get("number").asText());
        });

        System.out.println(uniqueCarNumber.size());

        return uniqueCarNumber;
    }


    private String split(String carNumber){
        StringBuilder builder = new StringBuilder(carNumber);

        builder.insert(2, "-");

        return builder.toString();
    }
}
