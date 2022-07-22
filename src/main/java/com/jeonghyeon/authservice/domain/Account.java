package com.jeonghyeon.authservice.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="accounts")
public class Account extends BaseTimeEntity{
    @Id
    @Column(name="account_pk")
    private String uuid;

    private String accountId;

    private String accountPassword;

    private String accountName;

    private String accountTel;

    private Long money;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @Embedded
    private Address address;
    protected Account(){}

    // 회원 가입 생성자
    public Account(String uuid,String accountId,String accountName,String accountTel,Address address){
        this.uuid = uuid;
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountTel = accountTel;
        this.money = (long) 0;
        this.accountRole = AccountRole.NORMAL;
        this.address = address;
    }

    public Account(String uuid,String adminId, String adminPw) {
        this.uuid = uuid;
        this.accountId = adminId;
        this.accountPassword = adminPw;
        this.accountRole = AccountRole.ADMIN;
    }
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public Account updateRole(String accountRole) {
        this.accountRole = AccountRole.valueOf(accountRole);
        return this;
    }
}
