package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.app_user.Role;

import java.util.List;

@Repository
public interface RoleDAO extends JpaRepository<Role, Integer> {

    List<Role> findByName(String name);
}
