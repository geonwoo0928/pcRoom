package com.example.pcRoom.constant;

import lombok.Getter;

@Getter
public enum Status {
    ADMIN("관리자"),
    USER("손님");

    private String status;

    Status(String status){
        this.status = status;
    }
}
