package personal.phuc.expense.security.user.registration.model;

import lombok.Data;
import personal.phuc.expense.security.user.registration.validation.PasswordMatches;
import personal.phuc.expense.security.user.registration.validation.ValidEmail;

@Data
public class RegistrationRequest {

    @ValidEmail
    private String email;

    @PasswordMatches
    private String password;
    private String matchingPassword;
}
