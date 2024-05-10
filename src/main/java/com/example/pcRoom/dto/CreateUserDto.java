package com.example.pcRoom.dto;

import com.example.pcRoom.constant.Status;
import com.example.pcRoom.entity.Users;
import lombok.Data;

@Data
public class CreateUserDto {
    private String userId; //회원 아이디
    private String name; //회원 이름
    private String password1; //회원 비밀번호
    private String password2; //비밀번호 재확인
    private String email; //회원 이메일
    private String phoneNum; //회원 전화번호
    private int confirmedNum; //관리자인증번호

//    public static Users dtoToEntity(CreateUserDto createUserDto){
//        return new Users(
//                createUserDto.getUserId(),
//                createUserDto.getName(),
//                createUserDto.getPassword1(),
//                createUserDto.getEmail(),
//                createUserDto.getPhoneNum(),
//                createUserDto.getStatus()
//        );
//    }
}
