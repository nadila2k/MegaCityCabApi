package com.nadila.MegaCityCab.config.jwt;

import com.nadila.MegaCityCab.service.AuthService.CabUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;



@Component
public class JwtUtil {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;


    public String generateTokenForUser(Authentication authentication){
        CabUserDetails cabUserDetailes = (CabUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(cabUserDetailes.getCabUser().getEmail())
                .claim("id", cabUserDetailes.getCabUser().getId())
                .claim("role", cabUserDetailes.getCabUser().getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() +expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());

        }
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
