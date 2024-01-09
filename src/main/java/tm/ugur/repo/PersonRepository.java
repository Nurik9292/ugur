package tm.ugur.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tm.ugur.models.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUserName(String userName);
}
