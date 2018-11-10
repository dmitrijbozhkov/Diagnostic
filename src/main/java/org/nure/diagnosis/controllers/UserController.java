package org.nure.diagnosis.controllers;

import org.nure.diagnosis.exceptions.InputDataValidationException;
import org.nure.diagnosis.exchangemodels.usercontroller.AuthToken;
import org.nure.diagnosis.exchangemodels.usercontroller.ChangePassword;
import org.nure.diagnosis.exchangemodels.usercontroller.CreateUserCredentials;
import org.nure.diagnosis.exchangemodels.usercontroller.UserCredentials;
import org.nure.diagnosis.models.enums.PersonAuthorities;
import org.nure.diagnosis.security.JwtTokenProvider;
import org.nure.diagnosis.services.interfaces.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Validator validator;
    @Autowired
    private IPersonService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    private void validateConstrains(Set<ConstraintViolation<Object>> validationErrors) {
        if (validationErrors.size() > 0) {
            throw new InputDataValidationException(validationErrors.iterator().next().getMessage());
        }
    }

    @RequestMapping(value = "/signin", method = { RequestMethod.POST })
    public ResponseEntity signin(@RequestBody CreateUserCredentials credentials) {
        Set<ConstraintViolation<Object>> validation = validator.validate(credentials);
        validateConstrains(validation);
        userService.createUser(
                Arrays.asList(PersonAuthorities.PATIENT),
                credentials.getName(),
                credentials.getSurname(),
                credentials.getLastName(),
                credentials.getGender(),
                credentials.getBirthday(),
                credentials.getEmail(),
                credentials.getPassword());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/login", method = { RequestMethod.POST })
    public ResponseEntity<AuthToken> login(@RequestBody UserCredentials credentials) {
        Set<ConstraintViolation<Object>> validation = validator.validate(credentials);
        validateConstrains(validation);
        Authentication auth = userService.authenticate(credentials.getEmail(), credentials.getPassword());
        String token = tokenProvider.generateToken(auth);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @Secured({"ROLE_PATIENT"})
    @RequestMapping(value = "/change-password", method = { RequestMethod.POST })
    public ResponseEntity changePassword(@RequestBody ChangePassword changePassword) {
        Set<ConstraintViolation<Object>> validation = validator.validate(changePassword);
        validateConstrains(validation);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.changePassword(auth.getName(), changePassword.getPassword(), changePassword.getOldPassword());
        return ResponseEntity.ok().build();
    }
}
