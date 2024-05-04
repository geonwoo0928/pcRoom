package com.example.pcRoom.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //주문번호

    @Column(nullable = false)
    private String userId; //회원아이디

    @Column(nullable = false)
    private int menuId; //메뉴 아이디

    @Column(nullable = false)
    private int sellAmount; //판매수량
}
