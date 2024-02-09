package tm.ugur.services.data_bus;

import tm.ugur.dto.BusDTO;

import java.util.List;

public interface BusDataProcessor {
    void processBusData(List<BusDTO> busData);
}
