package personal.phuc.expense.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.phuc.expense.entity.user.Role;

import java.util.List;

@Repository
public interface RoleDAO extends JpaRepository<Role, Integer> {

    List<Role> findByName(String name);
}
