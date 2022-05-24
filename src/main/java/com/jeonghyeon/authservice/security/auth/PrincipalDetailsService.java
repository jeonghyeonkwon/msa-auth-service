package com.jeonghyeon.authservice.security.auth;

import com.jeonghyeon.authservice.domain.Account;
import com.jeonghyeon.authservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> opAccount = accountRepository.findByAccountId(username);
        if(opAccount.isEmpty()) throw new UsernameNotFoundException("해당하는 유저는 없습니다");
        return new PrincipalDetails(opAccount.get());
    }
}
