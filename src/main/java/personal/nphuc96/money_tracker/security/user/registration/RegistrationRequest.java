package personal.nphuc96.money_tracker.security.user.registration;

import lombok.Data;
import personal.nphuc96.money_tracker.security.user.registration.validation.PasswordMatches;
import personal.nphuc96.money_tracker.security.user.registration.validation.ValidEmail;

@Data
public class RegistrationRequest {

    @ValidEmail
    private String email;

    @PasswordMatches
    private String password;
    private String matchingPassword;
}
