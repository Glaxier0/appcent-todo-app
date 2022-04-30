package com.glaxier.todo.security.jwt;

import java.util.Date;
import java.util.Optional;

import com.glaxier.todo.model.Users;
import com.glaxier.todo.services.UserDetailsImpl;
import com.glaxier.todo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.transaction.Transactional;

@Component
@Transactional
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;
    UserService userService;

    public JwtUtils(UserService userService) {
        this.userService = userService;
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Optional<Users> userData = userService.findByEmail(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
                    .getBody().getSubject());
            if (userData.isPresent()) {
                Users user = userData.get();
                if (user.getTokens().stream().anyMatch(authToken::equals)) {
                    return true;
                }
            }
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getToken(HttpHeaders httpHeaders) {
        String token = httpHeaders.get("authorization").get(0).substring(7);
        return token;
    }
}
