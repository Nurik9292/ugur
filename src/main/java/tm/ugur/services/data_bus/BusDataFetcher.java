package tm.ugur.services.data_bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.data_bus.import_data.AtLogisticImport;
import tm.ugur.services.data_bus.import_data.ImdataImport;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BusDataFetcher {

    private final ImdataImport imdataImport;
    private final AtLogisticImport atLogisticImport;

    @Autowired
    public BusDataFetcher(ImdataImport imdataImport, AtLogisticImport atLogisticImport) {
        this.imdataImport = imdataImport;
        this.atLogisticImport = atLogisticImport;
    }

    public List<BusDTO> fetchBusDataFromAllSources() {
        Map<String, BusDTO> imdateBuses = imdataImport.getBusData();
        Map<String, BusDTO> atLogistikaBuses = atLogisticImport.getBusData();
      
        return imdateBuses.entrySet().stream()
                .filter(entry -> atLogistikaBuses.containsKey(entry.getKey()))
                .map(entry -> {
                    BusDTO imdataBus = entry.getValue();
                    BusDTO atLogistikaBus = atLogistikaBuses.get(entry.getKey());
                    return new BusDTO(
                            1L,
                            imdataBus.getCarNumber(),
                            imdataBus.getNumber(),
                            atLogistikaBus.getSpeed(),
                            atLogistikaBus.getDir(),
                            atLogistikaBus.getLocation()
                    );
                })
                .sorted(Comparator.comparing(BusDTO::getNumber))
                .collect(Collectors.toList());
    }

}
