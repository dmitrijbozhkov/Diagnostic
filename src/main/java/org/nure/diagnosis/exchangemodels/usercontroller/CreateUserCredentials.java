package org.nure.diagnosis.exchangemodels.usercontroller;

import lombok.Value;
import org.nure.diagnosis.models.enums.Gender;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Value
public class CreateUserCredentials {

    @NotNull(message = "Name can't be empty")
    private String name;

    @NotNull(message = "Surname can't be empty")
    private String surname;

    @NotNull(message = "Last name can't be empty")
    private String lastName;

    @NotNull(message = "Gender can't be empty")
    private Gender gender;

    @NotNull(message = "Birthday can't be empty")
    private Date birthday;

    @NotNull(message = "Email can't be empty")
    @Email(message = "Please, provide valid email address")
    private String email;

    @NotNull(message = "Password can't be empty")
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Password should contain latin lower and upper case characters and numbers")
    private String password;
}
