package personal.phuc.expense.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.phuc.expense.dao.user.AppUserDAO;
import personal.phuc.expense.dao.user.RoleDAO;
import personal.phuc.expense.entity.user.AppUser;
import personal.phuc.expense.entity.user.RegistrationToken;
import personal.phuc.expense.exception.BadRequestException;
import personal.phuc.expense.exception.ResourceAlreadyExists;
import personal.phuc.expense.exception.ResourceNotFoundException;
import personal.phuc.expense.security.user.mail_service.Mail;
import personal.phuc.expense.security.user.registration.RegistrationServices;
import personal.phuc.expense.security.user.registration.model.RegistrationContent;
import personal.phuc.expense.security.user.registration.model.RegistrationRequest;
import personal.phuc.expense.security.user.mail_service.MailService;
import personal.phuc.expense.util.StringUtil;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService implements RegistrationServices {

    private final RegistrationTokenService registrationTokenService;
    private final AppUserDAO appUserDAO;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RoleDAO roleDAO;
    private final StringUtil stringUtil;
    private final int EXPIRATION_TIME = 59;

    @Transactional
    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        String subject = "Please verify your registration";
        comparePassword(request.getPassword(), request.getMatchingPassword());
        checkEmailExists(request.getEmail());
        AppUser appUser = initialAppUser(request);
        appUserDAO.save(appUser);
        RegistrationToken registrationToken = initialConfirmToken(appUser.getId());
        registrationTokenService.save(registrationToken);
        String content = mailService
                .registrationContent(getMailContent(appUser.getId(), registrationToken.getToken()));
        mailService.send(new Mail(appUser.getEmail(), content,subject));

    }

    private void comparePassword(String pass, String matchingPassword) {
        if (!pass.equals(matchingPassword)) {
            throw new BadRequestException("Password not matched");
        }
    }

    private void checkEmailExists(String email) {
        Optional<AppUser> appUser = appUserDAO.findByEmail(email);
        if (appUser.isPresent()) {
            throw new ResourceAlreadyExists("Email already taken");
        }
    }

    private AppUser initialAppUser(RegistrationRequest request) {
        return AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(false)
                .isNonLocked(false)
                .roles(roleDAO.findByName("USER"))
                .build();

    }

    private RegistrationToken initialConfirmToken(Integer userId) {
        int length = 30;
        String baseWord = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return RegistrationToken.builder()
                .userId(userId)
                .token(stringUtil.randomToken(length, baseWord))
                .creationTime(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plusMinutes(EXPIRATION_TIME))
                .isConfirmed(false)
                .confirmationTime(null)
                .build();
    }

    private RegistrationContent getMailContent(Integer userId, String token) {
        return new RegistrationContent(userId, token, EXPIRATION_TIME);
    }

    @Transactional
    @Override
    public void confirmToken(String token, Integer userId) {
        registrationTokenService.confirmToken(token, userId);
        enabledUser(userId);
    }

    private void enabledUser(Integer userId) {
        Optional<AppUser> appUser = appUserDAO.findById(userId);
        if (appUser.isPresent()) {
            appUser.get().setIsEnabled(true);
            appUser.get().setIsNonLocked(true);
        } else throw new ResourceNotFoundException("Not Found Id : " + userId);
    }


}
