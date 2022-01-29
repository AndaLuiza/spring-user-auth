package com.anda.user.auth.security.jwt;

import com.anda.user.auth.security.service.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utils class to generate and validate jwt token
 */
@Slf4j
@Component
public class JwtTokenUtils
{
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.validity.ms}")
    private int jwtTokenValidityMs;

    /**
     * Generates a jwt token based on authentication data
     *
     * @param authentication the authentication info
     * @return the token
     */
    public String generateJwtToken(Authentication authentication)
    {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((principal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtTokenValidityMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Checks if a jwt token is valid
     *
     * @param token the access token
     * @return if valid
     */
    public boolean validateJwtToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e)
        {
            log.error("Invalid JWT signature: {}", e.getMessage());
        }
        catch (MalformedJwtException e)
        {
            log.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e)
        {
            log.error("JWT token is expired: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e)
        {
            log.error("JWT token is unsupported: {}", e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Gets the username from the token
     *
     * @param token the access token
     * @return the subject
     */
    public String getUsernameFromJwtToken(String token)
    {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
