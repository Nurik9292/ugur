package tm.ugur.services.data_bus;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtLogisticService {

    private final BusResponseService responseService;

    @Autowired
    public AtLogisticService(BusResponseService responseService) {
        this.responseService = responseService;
    }

    public JsonNode getDataBus(){
        return this.responseService.getData("https://atlogistika.com/api/api.php?cmd=list",
                "turkmenavtoulag", "Awto996");
    }
}
