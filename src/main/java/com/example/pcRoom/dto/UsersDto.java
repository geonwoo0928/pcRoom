package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersDto {

    private Long userNo;
    private String userId;
    private String name;
    private String password;
    private int money;
    private String status; //관리자 , 일반


    //    Entity를 Dto로 변환
    public static UsersDto fromUserEntity(Users users) {
        return new UsersDto(
                users.getUserNo(),
                users.getUserId(),
                users.getName(),
                users.getPassword(),
                users.getMoney(),
                users.getStatus()
        );
    }

    //    Dto를 Entity로 변환
    public Users fromUserDto(UsersDto usersDto) {
        Users users = new Users();
        users.setUserNo(usersDto.getUserNo());
        users.setUserId(usersDto.getUserId());
        users.setName(usersDto.getName());
        users.setPassword(usersDto.getPassword());
        users.setMoney(usersDto.getMoney());
        users.setStatus(usersDto.getStatus());
        return users;
    }
}
