package org.nure.diagnosis.exchangemodels.usercontroller;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
public class ChangePassword {

    @NotNull(message = "New password can't be empty")
    @Pattern(regexp = "[A-Za-z0-9]+", message = "New password should contain latin lower and upper case characters and numbers")
    private String password;
    @NotNull(message = "Old password can't be empty")
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Password should contain latin lower and upper case characters and numbers")
    private String oldPassword;
}
