package com.jeonghyeon.authservice.service;

import com.jeonghyeon.authservice.domain.Account;
import com.jeonghyeon.authservice.domain.AccountRole;
import com.jeonghyeon.authservice.domain.Address;
import com.jeonghyeon.authservice.dto.AccountRequestDto;
import com.jeonghyeon.authservice.dto.ResponseDto;
import com.jeonghyeon.authservice.dto.RoleChangeRequestDto;
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
            throw new IllegalStateException("중복된 아이디가 있습니다");
        }

        return new ResponseEntity("사용가능한 아이디 입니다.",HttpStatus.OK);

    }

    @Transactional
    public String createAccount(AccountRequestDto dto) {

        String accountId = dto.getAccountId();
        Optional<Account> opAccount = accountRepository.findByAccountId(accountId);
        if(opAccount.isPresent()){
            throw new IllegalStateException("회원 가입 중 중복된 사용자가 있습니다.");
        }
        Address address = new Address(dto.getZipCode(),dto.getDetail());

        Account account = new Account(UUID.randomUUID().toString(),dto.getAccountId(),dto.getAccountName(),dto.getAccountTel(),address);
        String encodePassword = passwordEncoder.encode(dto.getAccountPassword());
        account.setAccountPassword(encodePassword);
        Account saveAccount = accountRepository.save(account);

        return saveAccount.getUuid();
    }


    public ResponseEntity adminCheck(String uuid) {
        Optional<Account> opAccount = accountRepository.findById(uuid);
        if(opAccount.isEmpty()){
            throw new IllegalStateException("해당 유저아이디에 대한 정보가 없습니다.");

        }
        Account account = opAccount.get();
        AccountRole accountRole = account.getAccountRole();
        if(accountRole==AccountRole.ADMIN)
            return new ResponseEntity(new ResponseDto<>(HttpStatus.OK.value(), accountRole.toString()),HttpStatus.OK);
        else
            throw new IllegalStateException("관리자가 아닙니다.");
    }
    @Transactional
    public ResponseEntity roleChange(RoleChangeRequestDto dto) {
        String accountId = dto.getAccountId();
        Optional<Account> opAccount = accountRepository.findByAccountId(accountId);
        if(opAccount.isEmpty()){
            return new ResponseEntity(accountId + " 해당 유저는 없습니다.",HttpStatus.BAD_REQUEST);
        }
        Account account = opAccount.get();
        Account updateAccount = account.updateRole(dto.getAccountRole());
        Account savedAccount = accountRepository.save(updateAccount);
        return new ResponseEntity(savedAccount.getUuid(),HttpStatus.OK);
    }
}
