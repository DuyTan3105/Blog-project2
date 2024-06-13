package com.example.blog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenHelper {
    private static final long JWT_TOKEN_VALIDITY = 60*60*1000;

    private String secret = "jwtTokenKey";

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> getClaim) {
        final Claims claims = getAllClaimsFromToken(token);
        return getClaim.apply(claims);
    }

    public String getUserNameFromToken(String token) {
        return getClaimFromToken(token,claims -> claims.getSubject());
    }

    public Date getExpirationDateFromToken (String token) {
        return getClaimFromToken(token,claims -> claims.getExpiration());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setClaims(new HashMap<String,Object>())
                .signWith(SignatureAlgorithm.HS256,secret)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+JWT_TOKEN_VALIDITY))
                .setSubject(userDetails.getUsername())
                .claim("roles",userDetails.getAuthorities())
                .compact();
    }

    public boolean isTokenNonExpirated(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.after(new Date());
    }

    public boolean validateToken(String token,UserDetails userDetails) {
        String userName = getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername()) && isTokenNonExpirated(token);

    }

}
