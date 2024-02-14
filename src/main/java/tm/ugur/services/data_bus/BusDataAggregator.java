package tm.ugur.services.data_bus;

import org.springframework.stereotype.Component;
import tm.ugur.dto.BusDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BusDataAggregator {

    public Map<Integer, List<BusDTO>> aggregateBusData(List<BusDTO> buses){
        Map<Integer, List<BusDTO>> map = new HashMap<>();
        for(BusDTO bus : buses){
            map.computeIfAbsent(bus.getNumber(), k -> new ArrayList<>()).add(bus);
        }
        return  map;
    }
}
