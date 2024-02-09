package tm.ugur.services.data_bus;

import tm.ugur.dto.BusDTO;

import java.util.Map;


public interface BusDataImport {
    Map<String, BusDTO> getBusData();
}
