package com.example.pcRoom.entity;

import com.example.pcRoom.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @Column(nullable = false, length = 20)
    private String userId; //회원아이디

    @Column(nullable = false, length = 10)
    private String name; //회원이름

    @Column(nullable = false)
    private String password; //회원비밀번호

    private String email; //회원이메일

    private String phoneNum; //회원 전화번호

    private int money; //잔액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status; //관리자 , 일반

//    public Users(String userId, String name, String password, String email, String phoneNum, Status status) {
//        this.userId = userId;
//        this.name = name;
//        this.password = password;
//        this.email = email;
//        this.phoneNum = phoneNum;
//        this.status = status;
//    }
}
