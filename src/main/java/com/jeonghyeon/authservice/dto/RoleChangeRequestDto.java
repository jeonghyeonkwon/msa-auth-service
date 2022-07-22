package com.jeonghyeon.authservice.dto;

import lombok.Data;

@Data
public class RoleChangeRequestDto {
    private String accountId;
    private String accountRole;
}
