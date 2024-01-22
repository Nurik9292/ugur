package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Bus;
import tm.ugur.repo.BusRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BusSservice {

    private final BusRepository busRepository;


    @Autowired
    public BusSservice(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> findAll(){
        return this.busRepository.findAll();
    }

    public Bus findOne(long id){
        return this.busRepository.findById(id).orElse(null);
    }

    public Bus findByCarNumber(String carNumber){
        return this.busRepository.findByCarNumber(carNumber).orElse(null);
    }

    @Transactional
    public void store(Bus bus){

        this.busRepository.save(bus);
    }

    @Transactional
    public void update(long id, Bus bus){
        bus.setId(id);
        this.busRepository.save(bus);
    }

    @Transactional
    public void delete(long id){
        this.busRepository.deleteById(id);
    }
}
