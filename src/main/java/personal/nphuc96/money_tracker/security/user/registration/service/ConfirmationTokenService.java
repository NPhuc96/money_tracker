package personal.nphuc96.money_tracker.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.dao.ConfirmationTokenDAO;
import personal.nphuc96.money_tracker.exception.BadRequestException;
import personal.nphuc96.money_tracker.exception.ResourceNotFoundException;
import personal.nphuc96.money_tracker.security.user.registration.model.ConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ConfirmationTokenService {

    private final ConfirmationTokenDAO confirmationTokenDAO;

    public void save(ConfirmationToken token) {
        confirmationTokenDAO.save(token);
        log.info("Saved Token : {}", token);
    }


    public void deleteConfirmedToken(ConfirmationToken token) {
        if (token.getIsConfirmed()) {
            confirmationTokenDAO.deleteByToken(token.getToken());
            log.info("Delete token object {}", token.toString());
        }
    }

    public void confirmTokenProcess(String token) {
        Optional<ConfirmationToken> temp = confirmationTokenDAO.findByToken(token);
        if (temp.isPresent()) {
            log.info("Found temp confirmationToken : {} ", temp.get().toString());
            ConfirmationToken confirmationToken = temp.get();
            checkConfirmed(confirmationToken.getIsConfirmed());
            checkConfirmedTime(confirmationToken.getConfirmationTime());
            checkExpiredTime(confirmationToken.getExpirationTime());
            confirmationToken.setConfirmationTime(LocalDateTime.now());
            confirmationToken.setIsConfirmed(true);
            confirmationTokenDAO.save(confirmationToken);
        } else throw new ResourceNotFoundException("Invalid Token, Not Found");
    }

    private void checkConfirmedTime(LocalDateTime confirmTime) {
        if (confirmTime != null) {
            throw new BadRequestException("Email already confirmed");
        }
    }

    private void checkExpiredTime(LocalDateTime expiredTime) {
        if (expiredTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired");
        }
    }

    private void checkConfirmed(boolean confirmed) {
        if (confirmed) {
            throw new BadRequestException("Token had been used");
        }
    }
}
