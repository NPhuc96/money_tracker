package personal.phuc.expense.security.user.password_reset.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import personal.phuc.expense.dao.user.AppUserDAO;
import personal.phuc.expense.dao.user.PasswordResetDAO;
import personal.phuc.expense.entity.user.AppUser;
import personal.phuc.expense.entity.user.PasswordResetCode;
import personal.phuc.expense.exception.BadRequestException;
import personal.phuc.expense.exception.FailedSQLExeption;
import personal.phuc.expense.exception.ResourceNotFoundException;
import personal.phuc.expense.security.user.mail_service.Mail;
import personal.phuc.expense.security.user.mail_service.MailService;
import personal.phuc.expense.security.user.password_reset.PasswordResetServices;
import personal.phuc.expense.security.user.password_reset.model.PasswordResetRequest;
import personal.phuc.expense.util.StringUtil;

import javax.mail.MessagingException;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class PasswordResetService implements PasswordResetServices {
    private final AppUserDAO appUserDAO;
    private final PasswordResetDAO passwordResetDAO;
    private final PasswordEncoder passwordEncoder;
    private final StringUtil stringUtil;
    private final MailService mailService;

    @Override
    public void requestReset(String email) throws MessagingException {
        String subject = "Your Verification Here";
        Optional<AppUser> appUser = appUserDAO.findByEmail(email);
        if (appUser.isPresent()) {
            String code = buildContent();
            mailService.send(new Mail(email, code, subject));
            passwordResetDAO.save(passwordResetCode(email, random()));
        } else throw new ResourceNotFoundException("Email is not exists");
    }

    @Override
    public void confirm(String code, String email) {
        try {
            PasswordResetCode passwordResetCode = passwordResetDAO.findByCodeAndEmail(code, email);
            if (passwordResetCode == null || passwordResetCode.getIsConfirmed()) {
                throw new BadRequestException("Code had been used");
            }
            passwordResetCode.setIsConfirmed(true);
            passwordResetDAO.save(passwordResetCode);
        } catch (FailedSQLExeption ex) {
            throw new FailedSQLExeption("Email or code is not exists");
        }
    }

    @Override
    public void reset(PasswordResetRequest request) {
        try {
            appUserDAO.updatePassword(request.getEmail(), passwordEncoder.encode(request.getNewPassword()));
        } catch (FailedSQLExeption ex) {
            throw new FailedSQLExeption("Could not update password");
        }
    }
    private PasswordResetCode passwordResetCode(String email,String content){
        return PasswordResetCode.builder()
                .email(email)
                .code(content)
                .isConfirmed(false)
                .build();
    }
    private String buildContent() {
        return mailService.passwordResetContent(random());
    }

    private String random() {
        int length = 6;
        String baseWord = "0123456789";
        return stringUtil.randomToken(length, baseWord);
    }
}
