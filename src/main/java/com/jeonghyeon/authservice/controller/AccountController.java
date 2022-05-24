package com.jeonghyeon.authservice.controller;

import com.jeonghyeon.authservice.dto.AccountRequestDto;
import com.jeonghyeon.authservice.dto.LoginDto;
import com.jeonghyeon.authservice.dto.TokenDto;
import com.jeonghyeon.authservice.security.jwt.JwtFilter;
import com.jeonghyeon.authservice.security.jwt.TokenProvider;
import com.jeonghyeon.authservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @GetMapping("/health-check")
    public ResponseEntity health_check(){
        System.out.println("OK");
        return new ResponseEntity("OK",HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity validateAccountId(@RequestParam("accountId")String accountId){
        return accountService.validateAccountId(accountId);
    }
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = tokenProvider.createToken(authentication);
        TokenDto tokenDto = new TokenDto(jwtToken);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER,tokenDto.getToken());
        return new ResponseEntity(tokenDto,httpHeaders,HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity register(@RequestBody AccountRequestDto dto){
        System.out.println(dto);
        return accountService.createAccount(dto);
    }
}
