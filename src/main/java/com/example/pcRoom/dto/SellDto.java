package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Sell;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SellDto {

    private Long id;
    private Long userNo;
    private Long menuId;
    private int sellAmount;

    private MenuDto menuDto;
    private UsersDto usersDto;

    private Integer total; // 총 매출

    public SellDto(Long id, Long userNo, Long menuId, int sellAmount, MenuDto menuDto, UsersDto usersDto) {
        this.id = id;
        this.userNo = userNo;
        this.menuId = menuId;
        this.sellAmount = sellAmount;
        this.menuDto = menuDto;
        this.usersDto = usersDto;
    }

    public SellDto(Long menuId){
        this.menuId = menuId;
        this.id = menuId; //시험삼아 지워야함
        } //사용 UserService - putMenuList sell테이블에 메뉴 아이디 등록


    public static Sell dtoToEntity(SellDto sellDto){
        return new Sell(
                sellDto.getId(),
                sellDto.getUserNo(),
                sellDto.getMenuId(),
                sellDto.sellAmount
        );
    }//사용 UserService - putMenuList sell테이블에 메뉴 아이디 등록

    public static SellDto fromSellEntity(Sell form, MenuDto menuDto, UsersDto usersDto, int sellAmount) {
        return new SellDto(
                form.getId(),
                form.getUserNo(),
                form.getMenuId(),
                form.getSellAmount(),
                menuDto,
                usersDto
        );
    }

    public SellDto(Integer total) {
        this.total = total;
    }
    public static SellDto fromTotal(Integer total) {
        return new SellDto(total);
    }
}