package personal.nphuc96.money_tracker.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.nphuc96.money_tracker.dto.JwtTokenResponse;
import personal.nphuc96.money_tracker.security.user.SecurityUser;
import personal.nphuc96.money_tracker.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
public class InitialAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;

    public InitialAuthenticationFilter(AuthenticationManager manager, JwtUtil jwtUtil) {
        this.manager = manager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        log.info("Trying to access with email : {}, password : {}", email, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        return manager.authenticate(authentication);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) auth.getPrincipal();
        log.info("Get SecurityUser from Authentication object : {}", user.toString());
        String token = jwtUtil.createToken(user, request);
        new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(response.getOutputStream(),
                        new JwtTokenResponse(token, jwtUtil.getId(user), jwtUtil.getTokenExpiration(token)));
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


}
