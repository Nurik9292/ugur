package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import tm.ugur.models.City;
import tm.ugur.repo.CityRepository;

@Controller
public class CitySeed implements CommandLineRunner {

    private final CityRepository cityRepository;

    @Autowired
    public CitySeed(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(this.cityRepository.findCityByName("Ашхабад").isEmpty()){
            this.cityRepository.save(new City("Ашхабад"));
            this.cityRepository.save(new City("Аркадаг"));
        }
    }
}
