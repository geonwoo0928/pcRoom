package com.example.pcRoom.entity;

import com.example.pcRoom.dto.SellDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //주문번호


    private Long userNo; //회원번호


    private Long menuId; //메뉴 아이디


    private int sellAmount; //판매수량

    public Sell(Long menuId , int sellAmount , Long userNo){
        this.menuId = menuId;
        this.sellAmount = sellAmount;
        this.userNo = userNo;
    } //사용 UserService - putMenuList sell테이블에 메뉴 아이디 등록
}
