package personal.phuc.expense.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import personal.phuc.expense.security.user.SecurityUser;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JwtUtil {


    @Value("${jwt.signing.key}")
    private String signature;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(signature);
    }

    private Date expiredTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }

    public String createToken(SecurityUser user, HttpServletRequest request) {
        try {
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(expiredTime())
                    .withIssuedAt(new Date())
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("Roles",
                            user.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList()))
                    .sign(getAlgorithm());
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Failed creating new token", exception.getCause());
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
            throw new JWTDecodeException("Failed extracting username");
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
            throw new JWTDecodeException("Failed extracting role");
        }
    }

    public LocalDateTime getTokenExpiration(String token) {
        try {
            Date expiration = decodedJWT(token).getExpiresAt();
            return LocalDateTime.ofInstant(
                    expiration.toInstant(), ZoneId.systemDefault());
        } catch (JWTDecodeException exception) {
            throw new JWTDecodeException("Failed extracting date");
        }
    }
}
