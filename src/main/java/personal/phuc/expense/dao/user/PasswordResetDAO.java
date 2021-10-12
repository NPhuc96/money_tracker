package personal.phuc.expense.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import personal.phuc.expense.entity.user.PasswordResetCode;

public interface PasswordResetDAO extends JpaRepository<PasswordResetCode, Integer> {

    @Query(value = "Select p from PasswordResetCode p where p.code=?1 and p.email=?2")
    PasswordResetCode findByCodeAndEmail(String code, String email);


}
