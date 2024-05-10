package com.example.pcRoom.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long menuId; //메뉴번호

    @Column(nullable = false)
    private String menuName; //메뉴이름

    @Column(nullable = false)
    private int menuAmount; //메뉴수량

    @Column(nullable = false)
    private int menuPrice; //메뉴가격

    private String menuKind; //메뉴종류 (라면,음료,간식,과자)
}
