package tm.ugur.services.data_bus;


import com.fasterxml.jackson.core.JsonProcessingException;

public interface ImportBusData {

    Object getDataBus() throws JsonProcessingException;
}
