package com.example.pcRoom.service;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    MenuRepository menuRepository;

    public List<MenuDto> showAllMenu() {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuDto> menuDtoList = new ArrayList<>();
        for(Menu menu : menuList){
            menuDtoList.add(
                    MenuDto.fromMenuEntity(menu)
            );
        }
        return menuDtoList;
    } // 메뉴 전체를 가져오는 메소드
}
