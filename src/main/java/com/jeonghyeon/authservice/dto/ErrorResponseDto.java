package com.jeonghyeon.authservice.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {
    private int statusCode;
    private String errorMsg;


    public ErrorResponseDto(int statusCode,String errorMsg){
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
    }
}