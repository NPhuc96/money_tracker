package personal.phuc.expense.security.user.registration;

import personal.phuc.expense.security.user.registration.model.RegistrationRequest;

import javax.mail.MessagingException;

public interface RegistrationServices {

    void register(RegistrationRequest request) throws MessagingException;

    void confirmToken(String token, Integer userId);

}
