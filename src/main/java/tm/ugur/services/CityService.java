package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.City;
import tm.ugur.repo.CityRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> findAll(){
        return this.cityRepository.findAll();
    }

    @Transactional
    public void store(City city){
        this.cityRepository.save(city);
    }

    @Transactional
    public void delete(int id){
        this.cityRepository.deleteById(id);
    }
}
