package com.example.pcRoom.dto;

import com.example.pcRoom.constant.Status;
import com.example.pcRoom.entity.Users;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    @Size(min = 3 , max = 15)
    @NotEmpty(message = "사용자 ID는 필수 입니다.")
    private String userId; //회원 아이디

    private String name; //회원 이름

    @NotEmpty(message = "비밀번호는 필수 입니다.")
    private String password1; //회원 비밀번호

    @NotEmpty(message = "비밀번호 확인은 필수 입니다.")
    private String password2; //비밀번호 재확인

    @NotEmpty(message = "이메일은 필수 입니다.")
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
