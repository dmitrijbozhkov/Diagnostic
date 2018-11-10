package org.nure.Diagnosis.Services.Interfaces;

import org.nure.Diagnosis.Exceptions.EntityAlreadyExistsException;
import org.nure.Diagnosis.Exceptions.EntityNotFoundException;
import org.nure.Diagnosis.Models.Enums.Gender;
import org.nure.Diagnosis.Models.Enums.PersonAuthorities;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;

public interface IPersonService {
    void createUser(List<PersonAuthorities> authorities, String name, String surname, String lastName, Gender gender, Date date, String email, String password) throws EntityAlreadyExistsException;
    Authentication authenticate(String username, String password);
    void changePassword(String email, String password, String oldPassword) throws EntityNotFoundException;
}
