package com.jeonghyeon.authservice.dto;

import lombok.Data;

@Data
public class AccountRequestDto {

    private String accountId;

    private String accountPassword;

    private String accountName;

    private String accountTel;

    private String zipCode;

    private String detail;

}
