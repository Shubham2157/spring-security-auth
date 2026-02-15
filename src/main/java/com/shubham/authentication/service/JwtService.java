package com.shubham.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {

    // write a function to generate jwt token using username details got from user

    private final static String SECRET = "qwertyuiopas1235dfghjklzx7781jjsdhcvbnm83482823";

    public String generateToken(String username){
        return Jwts.builder().subject(username).claims(new HashMap<>())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000*60*30))
                .signWith(geneateSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key geneateSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // verify the token from user and check expiry time

    public Claims validateTokenAndExtractClaims(String token){
        return Jwts.parser()
                .setSigningKey(geneateSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
