package com.example.pcRoom.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(nullable = false, length = 20)
    private String userId; //회원아이디

    @Column(nullable = false, length = 10)
    private String name; //회원이름

    @Column(nullable = false)
    private String password; //회원비밀번호

    private int money; //잔액

    @Column(nullable = false)
    private String status; //관리자 , 일반
}
