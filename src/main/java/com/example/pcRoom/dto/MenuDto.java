package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuDto {

    private Long menuId;
    private String menuName;
    private int menuAmount;
    private int menuPrice;

    // entity --> dto
    public static MenuDto fromMenuEntity(Menu menu) {
        return new MenuDto(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuAmount(),
                menu.getMenuPrice()
        );
    }
}

