package personal.phuc.expense.security.user.password_reset.model;

import lombok.Data;
import personal.phuc.expense.security.user.registration.validation.PasswordMatches;

@Data
public class PasswordResetRequest {

    private String email;

    @PasswordMatches
    private String newPassword;
}
