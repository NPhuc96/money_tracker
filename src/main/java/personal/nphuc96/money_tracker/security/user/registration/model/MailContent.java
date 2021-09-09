package personal.nphuc96.money_tracker.security.user.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailContent {
    private String to;
    private String verifyToken;
    private int expirationTime;
}
