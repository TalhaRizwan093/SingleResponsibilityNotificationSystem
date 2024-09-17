package com.toosterr.backend.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;


@Component
public class JwtUtil {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.secret-key.service}")
    private String secretKeyService;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        secretKeyService = Base64.getEncoder().encodeToString(secretKeyService.getBytes());
    }

    public boolean validateToken(String token, boolean fromService) {
        try {
            if(fromService) {
                Jwts.parser().setSigningKey(secretKeyService).parseClaimsJws(token);
            } else {
                Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            }
            return true;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            throw new SignatureException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new MalformedJwtException("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new UnsupportedJwtException("Unsupported JWT token");
        } catch(ExpiredJwtException ex){
            throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "The Token Provided is Expired");
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT claims string is empty");
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
