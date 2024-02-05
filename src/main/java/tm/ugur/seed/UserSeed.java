package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tm.ugur.models.Person;
import tm.ugur.models.Role;
import tm.ugur.repo.PersonRepository;

@Component
public class UserSeed implements CommandLineRunner {

    private final PersonRepository personRepository;

    @Autowired
    public UserSeed(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if(this.personRepository.findByUserName("super").isEmpty()){
            Person user = new Person();
            user.setUserName("super");
            user.setRole(Role.ROLE_ADMIN);
            user.setPassword("superAdmin123");
            this.personRepository.save(user);
        }
    }
}
