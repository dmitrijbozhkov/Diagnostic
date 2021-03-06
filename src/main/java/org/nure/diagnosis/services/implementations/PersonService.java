package org.nure.diagnosis.services.implementations;

import lombok.NoArgsConstructor;
import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.exceptions.InputDataValidationException;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.models.enums.PersonAuthorities;
import org.nure.diagnosis.models.Person;
import org.nure.diagnosis.repositories.IPersonRepository;
import org.nure.diagnosis.services.interfaces.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class PersonService implements IPersonService {
    @Autowired
    private IPersonRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public PersonService(IPersonRepository repo, AuthenticationManager manager, BCryptPasswordEncoder encoder) {
        this.userRepository = repo;
        this.authManager = manager;
        this.encoder = encoder;
    }

    private Person getPersonByEmail(String email) throws EntityNotFoundException {
        Optional<Person> search = userRepository.findByEmail(email);
        if (!search.isPresent()) {
            throw new EntityNotFoundException(String.format("User with name of %s already exists", email));
        }
        return search.get();
    }

    @Override
    @Transactional
    public void createUser(List<PersonAuthorities> authorities, String name, String surname, String lastName, Gender gender, Date date, String email, String password) throws EntityAlreadyExistsException {
        try {
            getPersonByEmail(email);
        } catch(EntityNotFoundException err) {
            List<String> encodedAuthorities = authorities
                    .stream()
                    .map((a) -> {
                        return a.getAuthority();
                    })
                    .collect(Collectors.toList());
            encodedAuthorities.add("Person");
            userRepository.save(new Person(encodedAuthorities, name, surname, lastName, gender, date, email, password));
            return;
        }
        throw new EntityAlreadyExistsException(String.format("User with email %s already exists, perhaps you should login?", email));
    }

    @Override
    public Authentication authenticate(String username, String password) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    @Override
    @Transactional
    public void changePassword(String email, String password, String oldPassword) throws EntityNotFoundException {
        Person user = getPersonByEmail(email);
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new InputDataValidationException("Old password isn't correct");
        }
        user.setPassword(encoder.encode(password));
    }

    @Override
    public boolean isEmailTaken(String email) {
        try {
            getPersonByEmail(email);
        }
        catch (EntityNotFoundException ex) {
            return false;
        }
        return true;
    }

}
