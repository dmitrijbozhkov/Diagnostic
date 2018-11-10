package org.nure.Diagnosis.Services.Implementations;

import org.nure.Diagnosis.Exceptions.EntityAlreadyExistsException;
import org.nure.Diagnosis.Exceptions.EntityNotFoundException;
import org.nure.Diagnosis.Exceptions.InputDataValidationException;
import org.nure.Diagnosis.Models.Enums.Gender;
import org.nure.Diagnosis.Models.Enums.PersonAuthorities;
import org.nure.Diagnosis.Models.Person;
import org.nure.Diagnosis.Repositories.IPersonRepository;
import org.nure.Diagnosis.Services.Interfaces.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService implements IPersonService {
    @Autowired
    private IPersonRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public PersonService() { }

    public PersonService(IPersonRepository repo, AuthenticationManager manager, BCryptPasswordEncoder encoder) {
        this.userRepository = repo;
        this.authManager = manager;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void createUser(List<PersonAuthorities> authorities, String name, String surname, String lastName, Gender gender, Date date, String email, String password) throws EntityAlreadyExistsException {
        Optional<Person> search = userRepository.findByEmail(email);
        if (search.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("GasStationUser with name of %s already exists", email));
        }
        List<String> encodedAuthorities = authorities
                .stream()
                .map((a) -> {
                    return a.getAuthority();
                })
                .collect(Collectors.toList());
        encodedAuthorities.add("Person");
        userRepository.save(new Person(encodedAuthorities, name, surname, lastName, gender, date, email, password));
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
        Optional<Person> search = userRepository.findByEmail(email);
        if (!search.isPresent()) {
            throw new EntityNotFoundException(String.format("User %s not found, can't change password", email));
        }
        Person user = search.get();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new InputDataValidationException("Old password isn't correct");
        }
        user.setPassword(encoder.encode(password));
    }

}
