package it.epicode.jwt.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.expirationms}")
    private Long jwtExpirationMs;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(Authentication auth) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl)auth.getPrincipal();
        Date now = new Date();
        Date expire = new Date(now.getTime() + jwtExpirationMs);
        userPrincipal.setExpirationTime(expire);

        return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(now).setExpiration(expire)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        }
        return false;
    }
}
