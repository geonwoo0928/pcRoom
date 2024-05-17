package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UpdateUserDto {

    private Long userNo; // 사용자 식별자

    @NotEmpty
    @Size(min = 3 , max = 15)
    private String userId = ""; //회원 아이디

    private String name = ""; //회원 이름

    private String password1; //회원 비밀번호

    private String password2; //비밀번호 재확인

    private String email = ""; //회원 이메일

    private String phoneNum = ""; //회원 전화번호

    private int confirmedNum; //관리자인증번호

    public Users toUserEntity(UpdateUserDto updateUserDto) {
        Users users = new Users();
        users.setUserNo(updateUserDto.getUserNo()); // 사용자 식별자 설정
        users.setUsername(updateUserDto.getUserId());
        users.setName(updateUserDto.getName());
        users.setPassword(updateUserDto.getPassword1()); // 비밀번호 수정 필요 시 사용할 수 있음
        users.setEmail(updateUserDto.getEmail());
        users.setPhoneNum(updateUserDto.getPhoneNum());
        return users;
    }
}
