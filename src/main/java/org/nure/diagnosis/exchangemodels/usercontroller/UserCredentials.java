package org.nure.diagnosis.exchangemodels.usercontroller;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class UserCredentials {

    @NotNull(message = "Email can't be empty")
    @Email(message = "Please, provide valid email address")
    private String email;

    @NotNull(message = "Password can't be empty")
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Password should contain latin lower and upper case characters and numbers")
    private String password;
}
