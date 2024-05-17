package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

    public Users toUserEntity() {
        Users users = new Users();
        users.setUserNo(this.userNo); // 사용자 식별자 설정
        users.setUsername(this.userId);
        users.setName(this.name);
        users.setPassword(this.password1); // 비밀번호 수정 필요 시 사용할 수 있음
        users.setEmail(this.email);
        users.setPhoneNum(this.phoneNum);
        return users;
    }
}
