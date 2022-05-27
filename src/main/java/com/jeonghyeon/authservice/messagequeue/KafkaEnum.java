package com.jeonghyeon.authservice.messagequeue;

import lombok.Data;
import lombok.Getter;

@Getter
public enum KafkaEnum {
    USER_INFO("auth-create-user-event");
    private String topic;
    KafkaEnum(String topic){
        this.topic = topic;
    }
}
