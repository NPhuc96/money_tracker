package personal.nphuc96.money_tracker.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import personal.nphuc96.money_tracker.security.user.SecurityUser;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JwtUtil {

    @Value("${jwt.expiration.time}")
    private Integer expiredTime;

    @Value("${jwt.signing.key}")
    private String signature;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(signature);
    }

    private Date expiredTime(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        log.info("Current time : {}", calendar.getTime());
        calendar.add(Calendar.HOUR, hours);
        log.info("Expired time : {}", calendar.getTime());
        return calendar.getTime();
    }

    public String createToken(SecurityUser user, HttpServletRequest request) {
        try {
            return JWT.create()
                    .withIssuer("Auth0 - Money Tracker Application")
                    .withExpiresAt(expiredTime(expiredTime))
                    .withSubject(user.getUsername())
                    .withIssuedAt(new Date())
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("Roles",
                            user.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()))
                    .sign(getAlgorithm());
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Can not create new token", exception.getCause());
        }
    }

    private DecodedJWT decodedJWT(String token) {
        JWTVerifier verifier = JWT
                .require(getAlgorithm())
                .acceptLeeway(1)
                .build();
        return verifier.verify(token);
    }

    public String getUsernameFromToken(String token) {
        try {
            return decodedJWT(token).getSubject();
        } catch (JWTDecodeException exception) {
            throw new JWTDecodeException("Invalid token");
        }
    }

    public Collection<SimpleGrantedAuthority> getRolesFromToken(String token) {
        try {
            List<String> roles = decodedJWT(token).getClaim("Roles").asList(String.class);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                authorities.add(new SimpleGrantedAuthority(role));
            }
            return authorities;

        } catch (JWTDecodeException exception) {
            throw new JWTDecodeException("Invalid token");
        }
    }
}
