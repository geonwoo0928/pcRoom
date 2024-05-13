package com.example.pcRoom.dto;

import com.example.pcRoom.entity.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuDto {

    private Long menuId;
    private String menuName;
    private int menuAmount;
    private int menuPrice;

    private String menuKind;
    private int quantity;

    // entity --> dto
    public static MenuDto fromMenuEntity(Menu menu) {
        return new MenuDto(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuAmount(),
                menu.getMenuPrice(),
                menu.getMenuKind()
        );
    }

    public MenuDto(Long menuId, String menuName, int menuAmount, int menuPrice, String menuKind) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuAmount = menuAmount;
        this.menuPrice = menuPrice;
        this.menuKind = menuKind;
    }
    public Menu fromMenuDto(MenuDto menuDto) {
        Menu menu = new Menu();
        menu.setMenuId(menuDto.getMenuId());
        menu.setMenuName(menuDto.getMenuName());
        menu.setMenuAmount(menuDto.getMenuAmount());
        menu.setMenuPrice(menuDto.getMenuPrice());
        menu.setMenuKind(menuDto.getMenuKind());

        return menu;
    }
}

