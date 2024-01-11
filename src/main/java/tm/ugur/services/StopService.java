package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Stop;
import tm.ugur.repo.StopRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StopService {

    private StopRepository stopRepository;

    @Autowired
    public StopService(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    public List<Stop> findAll(){
        return this.stopRepository.findAll();
    }

    public Optional<Stop> findOne(int id){
        return this.stopRepository.findById(id);
    }

    @Transactional
    public void store(Stop stop){
        this.stopRepository.save(stop);
    }

    @Transactional
    public void update(int id, Stop stop){
        stop.setId(id);
        this.stopRepository.save(stop);
    }

    @Transactional
    public void delete(int id){
        this.stopRepository.deleteById(id);
    }
}
