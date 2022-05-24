package com.jeonghyeon.authservice.domain;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="accounts")
@Data
public class Account extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_pk")
    private Long id;

    private String accountId;

    private String accountRandomId;

    private String accountPassword;

    private String accountName;

    private String accountTel;

    private Long price;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    @Embedded
    private Address address;
    protected Account(){}



    public Account(String accountRandomId,String accountId,String accountName,String accountTel,Address address){
        this.accountRandomId = accountRandomId;
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountTel = accountTel;
        this.price = (long) 0;
        this.accountRole = AccountRole.NORMAL;
        this.address = address;
    }

    public Account(String accountRandomId,String adminId, String adminPw) {
        this.accountRandomId= accountRandomId;
        this.accountId = adminId;
        this.accountPassword = adminPw;
        this.accountRole = AccountRole.ADMIN;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }


}
