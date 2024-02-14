package tm.ugur.services.data_bus.import_data;

import tm.ugur.dto.BusDTO;

import java.util.Map;


public interface BusDataImport {
    Map<String, BusDTO> getBusData();
}
