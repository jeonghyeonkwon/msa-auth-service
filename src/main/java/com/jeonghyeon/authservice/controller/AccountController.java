package com.jeonghyeon.authservice.controller;

import com.jeonghyeon.authservice.dto.*;
import com.jeonghyeon.authservice.messagequeue.KafkaProducer;
import com.jeonghyeon.authservice.security.jwt.JwtFilter;
import com.jeonghyeon.authservice.security.jwt.TokenProvider;
import com.jeonghyeon.authservice.security.util.SecurityUtil;
import com.jeonghyeon.authservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final KafkaProducer kafkaProducer;
    @GetMapping("/health-check")
    public ResponseEntity healthCheck(){

        return new ResponseEntity("OK",HttpStatus.OK);
    }
    @GetMapping("/admin-check")
    public ResponseEntity adminCheck(){
        Optional<String> currnetAccountUUID = SecurityUtil.getCurrnetAccountUUID();
        if(currnetAccountUUID.isEmpty()){
            throw new IllegalStateException("토큰이 비어있습니다.");
        }
        String uuid = currnetAccountUUID.get();
        return accountService.adminCheck(uuid);
    }
    @GetMapping("/user-info-check")
    public ResponseEntity userInfoCheck(){
        Optional<String> currnetAccountUUID = SecurityUtil.getCurrnetAccountUUID();
        if(currnetAccountUUID.isEmpty()){
            throw new IllegalStateException("토큰이 비어있습니다.");
        }
        String uuid = currnetAccountUUID.get();
        return new ResponseEntity(new ResponseDto<>(HttpStatus.OK.value(), uuid),HttpStatus.OK);
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
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AccountRequestDto dto){
        System.out.println(dto);
        String accountUUID = accountService.createAccount(dto);
        UserInfoDto userInfoDto = new UserInfoDto(dto.getAccountId(),
                accountUUID,
                dto.getAccountName(),
                dto.getAccountTel(),
                dto.getZipCode(),
                dto.getDetail());
        UserInfoDto userInfo = kafkaProducer.userInfoSend(userInfoDto);

        return new ResponseEntity(new ResponseDto<>(HttpStatus.CREATED.value(),accountUUID),HttpStatus.CREATED);
    }
    @GetMapping("/authentication-check")
    public ResponseEntity authenticationCheck(){
        return new ResponseEntity(new ResponseDto<>(HttpStatus.OK.value(), "OK"),HttpStatus.OK);
    }
}
