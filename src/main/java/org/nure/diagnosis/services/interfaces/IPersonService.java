package org.nure.diagnosis.services.interfaces;

import org.nure.diagnosis.exceptions.EntityAlreadyExistsException;
import org.nure.diagnosis.exceptions.EntityNotFoundException;
import org.nure.diagnosis.models.enums.Gender;
import org.nure.diagnosis.models.enums.PersonAuthorities;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface IPersonService {
    void createUser(List<PersonAuthorities> authorities, String name, String surname, String lastName, Gender gender, Date date, String email, String password) throws EntityAlreadyExistsException;
    Authentication authenticate(String username, String password);
    void changePassword(String email, String password, String oldPassword) throws EntityNotFoundException;
}
