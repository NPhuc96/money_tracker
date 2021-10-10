package personal.phuc.expense.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.phuc.expense.entity.user.RegistrationToken;

import java.util.Optional;

@Repository
public interface RegistrationTokenDAO extends JpaRepository<RegistrationToken, Integer> {

    @Query(value = "select r from RegistrationToken r where r.token=?1 and r.userId =?2")
    Optional<RegistrationToken> findByTokenAndUserId(String token, Integer userId);

}
