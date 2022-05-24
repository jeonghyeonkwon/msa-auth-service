package com.jeonghyeon.authservice.service;

import com.jeonghyeon.authservice.domain.Account;
import com.jeonghyeon.authservice.domain.Address;
import com.jeonghyeon.authservice.dto.AccountRequestDto;
import com.jeonghyeon.authservice.dto.ErrorResponseDto;
import com.jeonghyeon.authservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity validateAccountId(String accountId) {
        Optional<Account> opAccount = accountRepository.findByAccountId(accountId);
        if(opAccount.isPresent()){
            return new ResponseEntity(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "중복된 아이디가 있습니다"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("사용가능한 아이디 입니다.",HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity createAccount(AccountRequestDto dto) {

        String accountId = dto.getAccountId();
        Optional<Account> opAccount = accountRepository.findByAccountId(accountId);
        if(opAccount.isPresent()){
            return new ResponseEntity(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),"회원 가입 중 중복된 사용자가 있습니다."),HttpStatus.BAD_REQUEST);
        }
        Address address = new Address(dto.getZipCode(),dto.getDetail());

        Account account = new Account(UUID.randomUUID().toString(),dto.getAccountId(),dto.getAccountName(),dto.getAccountTel(),address);
        String encodePassword = passwordEncoder.encode(dto.getAccountPassword());
        account.setAccountPassword(encodePassword);
        Account saveAccount = accountRepository.save(account);

        return new ResponseEntity(saveAccount.getAccountId(),HttpStatus.CREATED);
    }


}
