package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.Person;
import tm.ugur.models.Role;
import tm.ugur.services.admin.PersonService;

@Component
@Order(2)
public class UserSeed implements CommandLineRunner {

    private final PersonService personService;

    @Autowired
    public UserSeed(PersonService personService) {
        this.personService = personService;
    }


    @Override
    public void run(String... args) throws Exception {
        if(this.personService.findByName("super").isEmpty()){
            Person user = new Person();
            user.setUserName("super");
            user.setRole(Role.ROLE_ADMIN);
            user.setPassword("superAdmin123");
            this.personService.store(user);
        }
    }
}
