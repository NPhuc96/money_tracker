package personal.phuc.expense.security.user.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationContent {
    private Integer userId;
    private String verifyToken;
    private int expirationTime;
}
