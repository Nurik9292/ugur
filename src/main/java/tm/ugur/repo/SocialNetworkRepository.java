package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.SocialNetwork;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {

    List<SocialNetwork> findByLink(String link);
}
