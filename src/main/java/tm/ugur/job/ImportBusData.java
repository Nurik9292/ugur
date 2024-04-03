package tm.ugur.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.data_bus.BusDataAggregator;
import tm.ugur.services.data_bus.BusDataFetcher;
import tm.ugur.services.data_bus.BusDestinationDirectionSide;
import tm.ugur.services.data_bus.BusIndexing;
import tm.ugur.services.data_bus.import_data.ImdataImport;
import tm.ugur.services.redis.RedisBusService;

import java.util.*;
import java.util.concurrent.locks.Lock;

@Component
public class ImportBusData {

    private final BusDataFetcher busDataFetcher;
    private final BusDataAggregator busDataAggregator;
    private final RedisBusService redisService;
    private final BusDestinationDirectionSide busSide;
    private final BusIndexing busIndexing;

    private final Lock lock;

    private static final Logger logger = LoggerFactory.getLogger(ImdataImport.class);

    @Autowired
    public ImportBusData(BusDataFetcher busDataFetcher,
                         BusDataAggregator busDataAggregator,
                         RedisBusService redisService,
                         BusDestinationDirectionSide busSide, BusIndexing busIndexing, Lock lock) {
        this.busDataFetcher = busDataFetcher;
        this.busDataAggregator = busDataAggregator;
        this.redisService = redisService;
        this.busSide = busSide;
        this.busIndexing = busIndexing;
        this.lock = lock;
    }

    @Scheduled(cron = "*/3 * 6-23 * * *")
    public void taskOne(){
        importData();
    }

    @Scheduled(cron = "*/4 * 6-23 * * *")
    public void taskTwo(){
        importData();
    }

    @Scheduled(cron = "*/5 * 6-23 * * *")
    public void taskThree(){
        importData();
    }

    @Scheduled(cron = "*/6 * 6-23 * * *")
    public void taskFor(){
        importData();
    }

    public void  importData(){
        lock.lock();
        try {
            Map<Integer, List<BusDTO>> aggregatedBuses = busDataAggregator.aggregateBusData(
                    busIndexing.indexing(busSide.define(busDataFetcher.fetchBusDataFromAllSources()))
            );
            for(Map.Entry<Integer, List<BusDTO>> entry : aggregatedBuses.entrySet()){
                redisService.addBuses(String.valueOf(entry.getKey()), entry.getValue());
            }
        } catch (Exception e) {
            logger.error("API unavailable job: " + e.getMessage());
        }finally {
            lock.unlock();
        }
    }


}
