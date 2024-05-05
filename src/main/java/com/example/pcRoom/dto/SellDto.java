package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Sell;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SellDto {

    private Long id;
    private String userId;
    private int menuId;
    private int sellAmount;

    private MenuDto menuDto;
    private UsersDto usersDto;
    private int total;

    public SellDto(Long id, String userId, int menuId, int sellAmount, MenuDto menuDto, UsersDto usersDto) {
        this.id = id;
        this.userId = userId;
        this.menuId = menuId;
        this.sellAmount = sellAmount;
        this.menuDto = menuDto;
        this.usersDto = usersDto;
    }

    public static SellDto fromSellEntity(Sell form, MenuDto menuDto, UsersDto usersDto, int sellAmount) {
        return new SellDto(
                form.getId(),
                form.getUserId(),
                form.getMenuId(),
                form.getSellAmount(),
                menuDto,
                usersDto
        );

    }

    public SellDto(int total) {
        this.total = total;
    }
    public static SellDto fromtotal(int total) {
        return new SellDto(
                total
        );
    }
}
