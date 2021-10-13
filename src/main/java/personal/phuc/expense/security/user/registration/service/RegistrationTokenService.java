package personal.phuc.expense.security.user.registration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import personal.phuc.expense.dao.user.RegistrationTokenDAO;
import personal.phuc.expense.entity.user.RegistrationToken;
import personal.phuc.expense.exception.BadRequestException;
import personal.phuc.expense.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationTokenService {

    private final RegistrationTokenDAO registrationTokenDAO;

    public void save(RegistrationToken token) {
        registrationTokenDAO.save(token);
    }

    public void confirmToken(String token, Integer userId) {
        Optional<RegistrationToken> temp = registrationTokenDAO.findByTokenAndUserId(token, userId);
        if (temp.isPresent()) {
            RegistrationToken registrationToken = temp.get();
            checkConfirmed(registrationToken.getIsConfirmed());
            checkConfirmedTime(registrationToken.getConfirmationTime());
            checkExpiredTime(registrationToken.getExpirationTime());
            registrationToken.setConfirmationTime(LocalDateTime.now());
            registrationToken.setIsConfirmed(true);
            registrationTokenDAO.save(registrationToken);
        } else throw new ResourceNotFoundException("Invalid Token");
    }

    private void checkConfirmed(boolean confirmed) {
        if (confirmed) {
            throw new BadRequestException("Token had been used");
        }
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


}
