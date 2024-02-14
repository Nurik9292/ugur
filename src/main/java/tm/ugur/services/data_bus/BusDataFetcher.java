package tm.ugur.services.data_bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.data_bus.import_data.AtLogisticImport;
import tm.ugur.services.data_bus.import_data.ImdataImport;

import java.util.*;

@Component
public class BusDataFetcher {

    private final ImdataImport imdataImport;
    private final AtLogisticImport atLogisticImport;

    @Autowired
    public BusDataFetcher(ImdataImport imdataImport, AtLogisticImport atLogisticImport) {
        this.imdataImport = imdataImport;
        this.atLogisticImport = atLogisticImport;
    }

    public List<BusDTO> fetchBusDataFromAllSources(){
        Map<String, BusDTO> imdateBuses = imdataImport.getBusData();
        Map<String, BusDTO> atLogistikaBuses = atLogisticImport.getBusData();
        List<BusDTO> buses = new ArrayList<>();

        for (Map.Entry<String, BusDTO> entry : imdateBuses.entrySet()) {
            BusDTO imdataBus = entry.getValue();
            if (atLogistikaBuses.containsKey(entry.getKey())){
                BusDTO atLogistikaBus = atLogistikaBuses.get(entry.getKey());
                buses.add(new BusDTO(
                        1L,
                        imdataBus.getCarNumber(),
                        imdataBus.getNumber(),
                        atLogistikaBus.getSpeed(),
                        atLogistikaBus.getDir(),
                        atLogistikaBus.getLocation()
                ));
            }
        }

        Collections.sort(buses, Comparator.comparing(BusDTO::getNumber));

        return buses;
    }


}
