package com.example.pcRoom.constant;

import lombok.Getter;

@Getter
public enum Status {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String status;

    Status(String status){
        this.status = status;
    }
}
