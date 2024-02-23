package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.City;
import tm.ugur.repo.CityRepository;

@Component
@Order(1)
@ConditionalOnProperty(name = "db.init.enabled", havingValue = "true", matchIfMissing = false)
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
