package com.jeonghyeon.authservice.domain;

import lombok.Getter;

@Getter
public enum AccountRole {
    ADMIN("관리자"),
    NORMAL("일반 유저"),
    MANAGER("지점장"),
    RIDER("라이더");

    private String kor;

    AccountRole(String kor){
        this.kor = kor;
    }
}
