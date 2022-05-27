package com.jeonghyeon.authservice.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private String accountId;

    private String accountRandomId;

    private String accountName;

    private String accountTel;

    private String zipCode;

    private String detail;

    public UserInfoDto (String accountId,
                        String accountRandomId,
                        String accountName,
                        String accountTel,
                        String zipCode,
                        String detail){
        this.accountId = accountId;
        this.accountRandomId = accountRandomId;
        this.accountName = accountName;
        this.accountTel = accountTel;
        this.zipCode = zipCode;
        this.detail = detail;
    }
}
