package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.nphuc96.money_tracker.dao.user.AppUserDAO;
import personal.nphuc96.money_tracker.dao.user.RoleDAO;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;
import personal.nphuc96.money_tracker.exception.BadRequestException;
import personal.nphuc96.money_tracker.exception.ResourceAlreadyExists;
import personal.nphuc96.money_tracker.exception.ResourceNotFoundException;
import personal.nphuc96.money_tracker.security.user.registration.RegistrationServices;
import personal.nphuc96.money_tracker.security.user.registration.model.ConfirmationToken;
import personal.nphuc96.money_tracker.security.user.registration.model.MailContent;
import personal.nphuc96.money_tracker.security.user.registration.model.RegistrationRequest;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class RegistrationService implements RegistrationServices {

    private final ConfirmationTokenService confirmationTokenService;
    private final AppUserDAO appUserDAO;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final RoleDAO roleDAO;
    @Value("${mail.expired.time}")
    private int expirationTime;
    @Value("${mail.confirmation.token}")
    private int tokenLength;
    @Value("${mail.confirmation.words}")
    private String words;

    @Transactional
    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        comparePassword(request.getPassword(), request.getMatchingPassword());
        checkEmailExists(request.getEmail());
        AppUser appUser = initialAppUser(request);
        appUserDAO.save(appUser);
        ConfirmationToken confirmationToken = initialConfirmToken(appUser.getId());
        confirmationTokenService.save(confirmationToken);
        String content = mailService
                .buildContent(getMailContent(appUser.getId(), confirmationToken.getToken()));
        sendEmail(appUser.getEmail(), content);
        log.info("Sent email to " + appUser.getEmail());

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
        AppUser appUser = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(false)
                .isNonLocked(false)
                .roles(roleDAO.findByName("USER"))
                .build();
        log.info("Built appUser : {}", appUser.toString());
        return appUser;

    }

    private ConfirmationToken initialConfirmToken(Integer userId) {
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .userId(userId)
                .token(randomToken())
                .creationTime(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plusMinutes(expirationTime))
                .isConfirmed(false)
                .confirmationTime(null)
                .build();
        log.info("Built confirmationToken : {}", confirmationToken.toString());
        return confirmationToken;
    }

    private String randomToken() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokenLength; i++) {
            sb.append(words.charAt(secureRandom.nextInt(words.length())));
        }
        log.info("Finished with the following string : {}", sb.toString());
        return sb.toString();
    }

    private MailContent getMailContent(Integer userId, String token) {
        return new MailContent(userId, token, expirationTime);
    }

    private void sendEmail(String email, String content) throws MessagingException {
        try {
            mailService.send(email, content);
        } catch (MessagingException ex) {
            throw new MessagingException("Server cant send email");
        }
    }

    @Transactional
    @Override
    public void confirmToken(String token, Integer userId) {
        confirmationTokenService.confirmToken(token, userId);
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
