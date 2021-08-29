package personal.nphuc96.money_tracker.security.filters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.nphuc96.money_tracker.security.user.SecurityUser;
import personal.nphuc96.money_tracker.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class InitialAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager manager;
    private JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getHeader("email");
        String password = request.getHeader("password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        return manager.authenticate(authentication);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) auth.getPrincipal();
        log.info("Get security user object : {} from Authentication object", user.toString());
        String token = jwtUtil.createToken(user);
        log.info("Generated new token for user : {}", user.toString());
        response.setHeader("access_token", token);
    }


}
