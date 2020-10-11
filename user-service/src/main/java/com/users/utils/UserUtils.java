package com.users.utils;

import com.users.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserUtils {

    public UUID generateUserId() {
        return UUID.randomUUID();
    }

    /**
     * Создание  emailVerificationToken
     **/
    public String generateEmailVerificationToken(String userId) {
        String token = Jwts.builder()
                .setSubject(userId)
//                time of email verification = 864000000; // 10 days
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }

    /**
     * проверка  срока действительности токена
     **/
    public boolean hasTokenExpired(String token) {
        boolean rez = false;
        try {
            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
                    .getBody();
            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();
            rez = tokenExpirationDate.before(todayDate);
        } catch (ExpiredJwtException ex) {
            rez = true;
        }
        return rez;
    }
}

