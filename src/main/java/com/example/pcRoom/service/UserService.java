package com.example.pcRoom.service;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.repository.MenuRepository;
import com.example.pcRoom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UsersRepository usersRepository;

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

    public List<UsersDto> usersList(){
        List<UsersDto> usersDto = new ArrayList<>();
        return usersRepository.findAll()
                .stream()
                .map(x -> UsersDto.fromUserEntity(x))
                .toList();
    } // 모든 회원 정보 출력
}
