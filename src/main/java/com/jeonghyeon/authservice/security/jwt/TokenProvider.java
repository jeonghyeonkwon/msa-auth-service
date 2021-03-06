package com.jeonghyeon.authservice.security.jwt;

import com.jeonghyeon.authservice.domain.Account;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Component
@Slf4j
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "auth";


    @Value("${token.expriation_time}")
    private long tokenValidityInMilliseconds;

    @Value("${token.secret}")
    private String secret;

    public String createToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Date expiration = new Date(System.currentTimeMillis()+tokenValidityInMilliseconds);


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY,authorities)
                .signWith(SignatureAlgorithm.HS512,this.secret)
                .setExpiration(expiration)
                .compact();
    }

    @Override
    public void afterPropertiesSet() throws Exception {}


    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (SignatureException | io.jsonwebtoken.MalformedJwtException e){
            log.info("????????? JWT ???????????????.");
        }catch (io.jsonwebtoken.ExpiredJwtException e){
            log.info("????????? JWT ???????????????");
        }catch (io.jsonwebtoken.UnsupportedJwtException e){
            log.info("???????????? ?????? JWT ???????????????.");
        }catch (IllegalArgumentException e){
            log.info("JWT ????????? ?????????????????????.");
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        System.out.println("claims : " + claims.get(AUTHORITIES_KEY));
        Collection<?extends GrantedAuthority> authorities  = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }
}

