package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import personal.nphuc96.money_tracker.dao.AppUserDAO;
import personal.nphuc96.money_tracker.dao.RoleDAO;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;
import personal.nphuc96.money_tracker.exception.BadRequestException;
import personal.nphuc96.money_tracker.exception.ResourceAlreadyExists;
import personal.nphuc96.money_tracker.exception.ResourceNotFoundException;
import personal.nphuc96.money_tracker.security.user.registration.RegistrationServices;
import personal.nphuc96.money_tracker.security.user.registration.model.ConfirmationToken;
import personal.nphuc96.money_tracker.security.user.registration.model.RegistrationRequest;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
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
    @Value("${mail.confirmation.token.words}")
    private String words;

    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        matchedPassword(request.getPassword(), request.getMatchingPassword());
        if (!isEmailExisted(request.getEmail())) {
            throw new ResourceAlreadyExists("Email already taken");
        }
        AppUser appUser = initialAppUser(request);
        log.info("Built appUser : {}", appUser.toString());
        appUserDAO.save(appUser);
        log.info("Saved  {}  to database ", appUser.toString());

        ConfirmationToken confirmationToken = initialConfirmToken(appUser.getId(), randomToken());
        log.info("Built confirmationToken : {}", confirmationToken.toString());
        confirmationTokenService.save(confirmationToken);
        log.info("Saved  {}  to database ", confirmationToken.toString());
        try {
            mailService.send(appUser.getEmail(), confirmationToken.getToken(), expirationTime);
            log.info("Sent email to " + appUser.getEmail());
        } catch (MessagingException ex) {
            throw new MessagingException("Something went wrong");
        }
    }

    @Override
    public void confirmToken(String token, String email) {
        confirmationTokenService.confirmTokenProcess(token);
        enabledUser(email);
    }

    private boolean isEmailExisted(String email) {
        Optional<AppUser> appUser = appUserDAO.findByEmail(email);
        return appUser.isEmpty();
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

    private AppUser initialAppUser(RegistrationRequest request) {
        return AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(false)
                .isNonLocked(false)
                .roles(roleDAO.findByName("USER"))
                .build();
    }

    private ConfirmationToken initialConfirmToken(Integer userId, String token) {
        return ConfirmationToken.builder()
                .userId(userId)
                .token(token)
                .creationTime(LocalDateTime.now())
                .expirationTime(LocalDateTime.now().plusMinutes(expirationTime))
                .isConfirmed(false)
                .confirmationTime(null)
                .build();
    }

    private void enabledUser(String email) {
        Optional<AppUser> appUser = appUserDAO.findByEmail(email);

        if (appUser.isPresent()) {
            appUser.get().setIsEnabled(true);
            appUser.get().setIsNonLocked(true);
            log.info("Successful confirmation " +
                            ", set enabled = {} , set non locked = {}"
                    , appUser.get().getIsEnabled()
                    , appUser.get().getIsNonLocked());
        } else throw new ResourceNotFoundException("Not Found Email : " + email);
    }

    private void matchedPassword(String pass, String matchingPassword) {
        if (!pass.equals(matchingPassword)) {
            throw new BadRequestException("Password not matched");
        }
    }
}
