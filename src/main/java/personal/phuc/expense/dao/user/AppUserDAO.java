package personal.phuc.expense.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import personal.phuc.expense.entity.user.AppUser;

import java.util.Optional;

@Repository
public interface AppUserDAO extends JpaRepository<AppUser, Integer> {

    @Query("SELECT u from AppUser u join fetch u.roles where u.email = ?1")
    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "Update AppUser a set a.password = ?2 where a.email=?1")
    void updatePassword(String email, String password);
}
