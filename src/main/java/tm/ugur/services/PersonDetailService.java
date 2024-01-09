package tm.ugur.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tm.ugur.models.Person;
import tm.ugur.repo.PersonRepository;
import tm.ugur.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {

    private PersonRepository personRepository;

    public PersonDetailService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> user = this.personRepository.findByUserName(username);
        return user.map(PersonDetails::new).orElseThrow(() -> new UsernameNotFoundException("Нет такого пользователя"));
    }
}
