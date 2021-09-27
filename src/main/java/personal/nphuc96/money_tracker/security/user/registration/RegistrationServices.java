package personal.nphuc96.money_tracker.security.user.registration;

import personal.nphuc96.money_tracker.security.user.registration.model.RegistrationRequest;

import javax.mail.MessagingException;

public interface RegistrationServices {

    void register(RegistrationRequest request) throws MessagingException;

    void confirmToken(String token, Integer userId);

}
