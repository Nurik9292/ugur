package tm.ugur.job;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tm.ugur.models.Bus;
import tm.ugur.services.BusSservice;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;

import java.util.*;
import java.util.concurrent.locks.Lock;

@Service
@EnableAsync
public class BusScheduling {

    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;
    private final BusSservice busSservice;
    private final Lock  lock;
    StringBuffer carNumbmer;
    StringBuffer number;


    private final static Logger logger = LoggerFactory.getLogger(BusScheduling.class);


    @Autowired
    public BusScheduling(ImdataService imdataService, AtLogisticService atLogisticService, BusSservice busSservice, Lock lock) {
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.busSservice = busSservice;
        this.lock = lock;
        this.carNumbmer = new StringBuffer();
        this.number = new StringBuffer();
    }


    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void scheduleFixedDelayTask(){
        try {
            Map<String, String> map = this.imdataService.getDataBus();
            JsonNode jsonNode = this.atLogisticService.getDataBus();


                for (JsonNode node : jsonNode.get("list")) {
                    this.lock.lock();
                    try {
                        if (!node.get("vehiclenumber").asText().isEmpty()) {
                            if (!carNumbmer.isEmpty()) {
                                carNumbmer.append(node.get("vehiclenumber").asText().trim());
                            }
                            carNumbmer.replace(0, carNumbmer.length(), node.get("vehiclenumber").asText().trim());
                        }
                        if(this.number.isEmpty()){
                            this.number.append(map.get(carNumbmer.toString()));
                        }else{
                            this.number.replace(0, this.number.length(), map.get(carNumbmer.toString()));
                        }

                        if (map.containsKey(carNumbmer.toString())) {
                            Bus bus = new Bus(
                                    carNumbmer.toString(),
                                    Integer.parseInt(this.number.toString()),
                                    node.get("status").get("speed").asText(),
                                    node.get("imei").asText(),
                                    node.get("status").get("dir").asText(),
                                    node.get("status").get("lat").asText(),
                                    node.get("status").get("lon").asText()
                            );


                            Optional<Bus> busUpdate = this.busSservice.findByCarNumber(carNumbmer.toString());
                            if (busUpdate.isEmpty()) {
                                this.busSservice.store(bus);
                            } else {
                                this.busSservice.update(busUpdate.get().getId(), bus);
                            }
                        }
                    }finally {
                        this.lock.unlock();
                    }
                }
        } catch (Exception e) {
            logger.error("API unavailable: " + e.getMessage());
            this.busSservice.deleteAll();
        }
    }
}