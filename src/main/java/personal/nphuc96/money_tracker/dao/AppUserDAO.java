package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;

import java.util.Optional;

@Repository
public interface AppUserDAO extends JpaRepository<AppUser, Integer> {

    @Query("SELECT u from AppUser u join fetch u.roles where u.email = ?1")
    Optional<AppUser> findByEmail(String email);
}
