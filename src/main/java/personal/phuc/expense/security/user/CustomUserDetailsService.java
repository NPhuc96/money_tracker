package personal.phuc.expense.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import personal.phuc.expense.dao.user.AppUserDAO;
import personal.phuc.expense.entity.user.AppUser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserDAO appUserDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> temp = appUserDAO.findByEmail(email);
        if (temp.isEmpty()) {
            throw new UsernameNotFoundException("Invalid email");
        }
        return new SecurityUser(temp.get());
    }
}
