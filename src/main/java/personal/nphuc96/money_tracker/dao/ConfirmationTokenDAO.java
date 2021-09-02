package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.security.user.registration.model.ConfirmationToken;

import java.util.Optional;

@Repository
public interface ConfirmationTokenDAO extends JpaRepository<ConfirmationToken, Integer> {

    Optional<ConfirmationToken> findByToken(String token);

    void deleteByToken(String token);
}
