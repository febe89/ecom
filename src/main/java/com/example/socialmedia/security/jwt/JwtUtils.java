package com.example.socialmedia.security.jwt;

import com.example.socialmedia.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${spring.jwtSecret}")
    private String jwtSecret;
    @Value("${spring.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.jwtCookie}")
    private String jwtCookie;

//    public String getJwtTokenFromHeader(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal){
        String jwt= generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie= ResponseCookie.from(jwtCookie,jwt)
                .path("/api").maxAge(24*60*60).httpOnly(false).build();
        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie= WebUtils.getCookie(request,jwtCookie);
            if(cookie!=null){
                return cookie.getValue();
            }else {
                return null;
            }

    }



    public String generateTokenFromUsername(String username) {
//        String username=userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpirationMs)))
                .signWith(key())
                .compact();
    }



    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return true;

    }


    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


}
