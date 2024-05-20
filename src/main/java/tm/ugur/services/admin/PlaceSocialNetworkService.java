package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.SocialNetwork;
import tm.ugur.repo.SocialNetworkRepository;

import java.util.Date;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PlaceSocialNetworkService {

    private final SocialNetworkRepository socialNetworkRepository;

    @Autowired
    public PlaceSocialNetworkService(SocialNetworkRepository socialNetworkRepository) {
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @Transactional
    public SocialNetwork store(SocialNetwork socialNetwork) {
        socialNetwork.setCreatedAt(new Date());
        socialNetwork.setUpdatedAt(new Date());
        return socialNetworkRepository.save(socialNetwork);
    }


    @Transactional
    public void deleteAll(Set<SocialNetwork> socialNetworks) {
        socialNetworkRepository.deleteAll(socialNetworks);
    }
}
