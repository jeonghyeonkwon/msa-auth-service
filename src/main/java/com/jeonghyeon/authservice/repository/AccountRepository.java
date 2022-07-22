package com.jeonghyeon.authservice.repository;

import com.jeonghyeon.authservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {
    Optional<Account> findByAccountId(String accountId);

    @Query("SELECT account.accountId" +
            " FROM Account account" +
            " WHERE account.accountName = :accountName" +
            " AND account.accountTel = :accountTel")
    Optional<String> forgetId(String accountName, String accountTel);


}
