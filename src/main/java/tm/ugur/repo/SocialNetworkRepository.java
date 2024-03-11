package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.ugur.models.SocialNetwork;

@Repository
public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {
}
