package com.jeonghyeon.authservice.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String zipCode;
    private String detail;
    protected Address(){}
    public Address(String zipCode,String detail){
        this.zipCode = zipCode;
        this.detail = detail;
    }
}