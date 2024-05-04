package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersDto {

    private String userId;
    private String name;
    private String password;
    private int money;
    private String status; //관리자 , 일반

    public static UsersDto fromUserEntity(Users users) {
        return new UsersDto(
                users.getUserId(),
                users.getName(),
                users.getPassword(),
                users.getMoney(),
                users.getStatus()
        );
    }
}
