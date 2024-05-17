package com.example.pcRoom.dto;

import com.example.pcRoom.constant.Status;
import com.example.pcRoom.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    private Long userNo;
    private String userId;
    private String name;
    private String password;
    private int money;
    private Status status; //관리자 , 일반
    private int totalMoney;

    public UsersDto(Long userNo, String userId, String name, String password, int money, String status){
    }

    public UsersDto(Long userNo, String userId, String name, String password, int money, Status status) {
        this.userNo = userNo;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.money = money;
        this.status = status;
    }

    //    Entity를 Dto로 변환
    public static UsersDto fromUserEntity(Users users) {
        if (users == null) {
            return new UsersDto(null, "탈퇴한 회원입니다", "탈퇴한 회원입니다", "", 0, "탈퇴");
        }
        return new UsersDto(
                users.getUserNo(),
                users.getUsername(),
                users.getName(),
                users.getPassword(),
                users.getMoney(),
                users.getStatus()
        );
    }

    //    Dto를 Entity로 변환
    public Users fromUserDto(UsersDto usersDto) {
        return new Users(
                usersDto.getUserNo(),
                usersDto.getUserId(),
                usersDto.getName(),
                usersDto.getPassword(),
                usersDto.getMoney(),
                usersDto.getStatus()
        );
    }
}
