package personal.nphuc96.money_tracker.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.app_user.ConfirmationToken;

import java.util.Optional;

@Repository
public interface ConfirmationTokenDAO extends JpaRepository<ConfirmationToken, Integer> {

    @Query(value = "select t from ConfirmationToken t where t.token=?1 and t.userId =?2")
    Optional<ConfirmationToken> findByTokenAndUserId(String token, Integer userId);

    void deleteByToken(String token);
}
