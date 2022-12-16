package com.example.demo.util;


import java.util.Date;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.Models.User;

import lombok.extern.log4j.Log4j2;
@Component
@Log4j2
public class JwtUtil {
    private  String jwtSecret ="mysecret";
    private  int jwtExpirationMs=86400000;

    //fuction to generate the jwt tokens
    public String generateJwtToken(Authentication authentication){
        //getting user details
        User user = (User) authentication.getPrincipal();
        //setting the subject and experation and signing wih the jwt secret
        return Jwts.builder().setSubject((user.getUsername())).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
    }
    //get the username form the jwt token
    public String getUserNameFromJwtToken(String token){
        //sby setting the key we use it to parse the tokens getting the body and the subject
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    //validate jwttoken
    public boolean validateJwtToken(String authToken){
        try{
            //try the parsed jwt if it is ok we return true
            Jwts.parser().setSigningKey(jwtSecret).parse(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e){
            log.error("Invalid JWT Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e){
            log.error("JWT unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
