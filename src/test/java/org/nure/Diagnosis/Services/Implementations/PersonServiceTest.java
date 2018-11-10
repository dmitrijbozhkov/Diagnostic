package org.nure.Diagnosis.Services.Implementations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.nure.Diagnosis.Exceptions.EntityAlreadyExistsException;
import org.nure.Diagnosis.Exceptions.EntityNotFoundException;
import org.nure.Diagnosis.Exceptions.InputDataValidationException;
import org.nure.Diagnosis.Models.Enums.Gender;
import org.nure.Diagnosis.Models.Enums.PersonAuthorities;
import org.nure.Diagnosis.Models.Person;
import org.nure.Diagnosis.Repositories.IPersonRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

    private IPersonRepository personRepository;
    private PersonService personService;
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private SecurityContextHolder securityContextHolder;

    private final String correctEmail = "matviei";
    private final String correctPassword = "pass1234";
    private final String name = "pepe";
    private final String surname = "kek";
    private final String lastName = "memes";
    private final Gender gender = Gender.FEMALE;
    private final Date birthday = Date.from(Instant.now());
    private final List<String> personAuthorities = Arrays.asList("Person", PersonAuthorities.PATIENT.getAuthority(), PersonAuthorities.DOCTOR.getAuthority());

    private final String changingPassword = "changetome";

    @Before
    public void setupRepo() {
        this.authenticationManager = mock(AuthenticationManager.class);
        this.personRepository = mock(IPersonRepository.class);
        this.personService = new PersonService(this.personRepository, this.authenticationManager, new BCryptPasswordEncoder());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateUserShouldThrowEntityAlreadyExistsExceptionIfUserExists() {
        given(personRepository.findByEmail(correctEmail))
                .willReturn(Optional
                        .of(new Person(
                                personAuthorities,
                                name,
                                surname,
                                lastName,
                                gender,
                                birthday,
                                correctEmail,
                                correctPassword)));
        personService
                .createUser(
                        PersonAuthorities.parseAuthorities(personAuthorities),
                        name,
                        surname,
                        lastName,
                        gender,
                        birthday,
                        correctEmail,
                        correctPassword);
    }

    @Test
    public void testCreateUserShouldSaveNewUserIfEverythingCorrect() {
        given(personRepository.findByEmail(correctEmail)).willReturn(Optional.empty());
        personService
                .createUser(
                        PersonAuthorities.parseAuthorities(personAuthorities),
                        name,
                        surname,
                        lastName,
                        gender,
                        birthday,
                        correctEmail,
                        correctPassword);
        verify(personRepository).save(isA(Person.class));
    }

    @Test
    public void testAuthenticateShouldAuthenticateUserByCredentials() {
        Authentication auth = mock(Authentication.class);
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(correctEmail, correctPassword))).willReturn(auth);
        Authentication givenAuth = personService.authenticate(correctEmail, correctPassword);
        assertEquals(auth, givenAuth);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testChangePasswordShouldThrowEntityNotFoundExceptionIfUserNotFound() {
        given(personRepository.findByEmail(correctEmail)).willReturn(Optional.empty());
        personService.changePassword(correctEmail, changingPassword, correctPassword);
    }

    @Test(expected = InputDataValidationException.class)
    public void testChangePasswordShouldThrowInputDataValidationExceptionIfOldPasswordIsWrong() {
        String wrongPassword = "sdfsgdfgdfg";
        Person user = spy(new Person(
                personAuthorities,
                name,
                surname,
                lastName,
                gender,
                birthday,
                correctEmail,
                wrongPassword));
        given(personRepository.findByEmail(correctEmail)).willReturn(Optional.of(user));
        personService.changePassword(correctEmail, changingPassword, correctPassword);
    }

    @Test
    public void testChangePasswordShouldChangePasswordInEntity() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Person user = spy(new Person(
                personAuthorities,
                name,
                surname,
                lastName,
                gender,
                birthday,
                correctEmail,
                encoder.encode(correctPassword)));
        given(personRepository.findByEmail(correctEmail)).willReturn(Optional.of(user));
        personService.changePassword(correctEmail, changingPassword, correctPassword);
        verify(user).setPassword(argThat(settingPassword -> encoder.matches(changingPassword, settingPassword)));
    }
}
