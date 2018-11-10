package org.nure.Diagnosis.Security;

import org.nure.Diagnosis.Models.Person;
import org.nure.Diagnosis.Repositories.IPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class DiagnosisUserDetailsService implements UserDetailsService {

    @Autowired
    private IPersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> search = personRepository.findByEmail(username);
        if (!search.isPresent()) {
            throw new UsernameNotFoundException(String.format("GasStationUser by username %s not found", username));
        }
        return new DiagnosisUserPrincipal(search.get());
    }
}
