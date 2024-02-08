package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Person;
import tm.ugur.repo.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private PersonRepository personRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> index(){
        return this.personRepository.findAll();
    }

    public Person findOne(int id){
        return this.personRepository.findById(id).orElse(null);
    }

    public Optional<Person> findByName(String name){
        return this.personRepository.findByUserName(name);
    }

    @Transactional
    public void store(Person user){
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.personRepository.save(user);
    }

    @Transactional
    public void upodate(int id, Person user){
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setId(id);
        this.personRepository.save(user);
    }

    @Transactional
    public void delete(int id){
        this.personRepository.deleteById(id);
    }
}
