package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Bus;
import tm.ugur.repo.BusRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Bus>  findByCarNumber(String carNumber){
        return this.busRepository.findByCarNumber(carNumber);
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


    @Transactional
    public void  deleteAll(){
        this.busRepository.deleteAll();
    }
}
