package personal.nphuc96.money_tracker.security.user.registration;

import javax.mail.MessagingException;

public interface RegistrationServices {

    void register(RegistrationRequest request) throws MessagingException;

    void confirmToken(String token, String email);

}
