package com.example.pcRoom.entity;

import com.example.pcRoom.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(nullable = false, name = "user_id", length = 20)
    private String username; //회원아이디

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

    public Users(Long userNo, String username, String name, String password, int money , Status status) {
        this.userNo = userNo;
        this.username = username;
        this.name = name;
        this.password = password;
        this.money = money;
        this.status = status;
    }
}
