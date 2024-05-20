package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.place.PlacePhone;
import tm.ugur.repo.PlacePhoneRepository;

import java.util.Date;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PlacePhoneService {

    private final PlacePhoneRepository placePhoneRepository;

    @Autowired
    public PlacePhoneService(PlacePhoneRepository placePhoneRepository) {
        this.placePhoneRepository = placePhoneRepository;
    }

    @Transactional
    public PlacePhone store(PlacePhone placePhone) {
        placePhone.setCreatedAt(new Date());
        placePhone.setUpdatedAt(new Date());
        return placePhoneRepository.save(placePhone);
    }


    @Transactional
    public void deleteAll(Set<PlacePhone> phones) {
        placePhoneRepository.deleteAll(phones);
    }
}
