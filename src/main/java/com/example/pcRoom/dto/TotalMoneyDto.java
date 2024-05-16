package com.example.pcRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
public class TotalMoneyDto {
    private Long userNo;
    private int totalMoney;
    private UsersDto usersDto;

    @Setter
    private Integer rank;

    public TotalMoneyDto(Long userNo, int totalMoney, UsersDto usersDto) {
        this.userNo = userNo;
        this.totalMoney = totalMoney;
        this.usersDto = usersDto;
    }

    public static TotalMoneyDto fromTotalMoney(Long userNo, int totalMoney, UsersDto usersDto) {
        return new TotalMoneyDto(
                userNo,totalMoney,usersDto
        );
    }
}
