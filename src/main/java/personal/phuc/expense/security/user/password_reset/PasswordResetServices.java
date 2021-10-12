package personal.phuc.expense.security.user.password_reset;

import personal.phuc.expense.security.user.password_reset.model.PasswordResetRequest;

import javax.mail.MessagingException;

public interface PasswordResetServices {
    void requestReset(String email) throws MessagingException;

    void confirm(String code, String email);

    void reset(PasswordResetRequest request);


}
