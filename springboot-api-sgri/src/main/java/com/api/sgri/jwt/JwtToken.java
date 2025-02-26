package com.api.sgri.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;


public class JwtToken {

    private static final String SECRET_KEY = "Diseno_de_Sistemas_de_Informacion_2024";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    public static Builder generateToken() {
        return new Builder();
    }

    public static Claims getPayload(String token) {
        try {

            Claims claims = Jwts
                            .parser()
                            .setSigningKey(SECRET_KEY)
                            .parseClaimsJws(token)
                            .getBody();
                        
            if(claims.getExpiration().before(new Date())) {
                throw new IllegalArgumentException(); 
            }

            return claims;

        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }


    public static class Builder {

        private Map<String, Object> claims;
        private String subject;
        private long time;

        public Builder() {
            this.time = EXPIRATION_TIME;
            this.claims = new HashMap<>();
        }

        public Builder addClaim(String key, Object value) {
            claims.put(key, value);
            return this;
        }


        public Builder setSubject(String value) {
            this.subject = value;
            return this; 
        }

        public Builder setTimeMinutes(int value) {
            this.time = (long) value * 60 * 1000;
            return this;
        }

        public Builder setTimeHours(int value) {
            this.time = (long) value * 60 * 60 * 1000;
            return this;
        }

        public String build() {
            return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + time))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
        }
    }
}
