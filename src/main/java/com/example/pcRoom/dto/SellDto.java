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
    private int orderAmount;

    private MenuDto menuDto;
    private UsersDto usersDto;

    public static SellDto fromOrderEntity(Sell form, MenuDto menuDto, UsersDto usersDto, int orderAmount) {
        return new SellDto(
                form.getId(),
                form.getUserId(),
                form.getMenuId(),
                form.getSellAmount(),
                menuDto,
                usersDto
        );

    }

}
