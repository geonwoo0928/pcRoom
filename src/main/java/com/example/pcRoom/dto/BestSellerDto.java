package com.example.pcRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class BestSellerDto {
    private Long menuId;
    private int sumSellAmount;
    private MenuDto menuDto;

    @Setter
    private Integer rank;

    public BestSellerDto(Long menuId, int sumSellAmount, MenuDto menuDto) {
        this.menuId = menuId;
        this.sumSellAmount = sumSellAmount;
        this.menuDto = menuDto;
    }

    public static BestSellerDto fromBestEntity(Long menuId, int sumSellAmount, MenuDto menuDto) {
        return new BestSellerDto(
                menuId,sumSellAmount,menuDto
        );
    }

}
