package com.jeonghyeon.authservice.security.auth;

import com.jeonghyeon.authservice.domain.Account;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {
    private Account account;
    public PrincipalDetails(Account account){
        this.account = account;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collector = new ArrayList<>();
        collector.add(()->"ROLE_"+account.getAccountRole().toString());
        return collector;
    }

    @Override
    public String getPassword() {
        return account.getAccountPassword();
    }

    @Override
    public String getUsername() {
        return account.getUuid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
