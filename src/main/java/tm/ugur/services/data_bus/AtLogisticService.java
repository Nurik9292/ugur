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
        String url = "http://atlogistika.com/api/api.php?cmd=list";
        return this.responseService.getData(url, "turkmenavtoulag", "Awto996");
    }
}
