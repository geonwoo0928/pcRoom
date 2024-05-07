package com.example.pcRoom.controller;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PcControllerJson {
    @Autowired
    UserService userService;

    @PostMapping("/user/userMenu")
    @ResponseBody
    public String purchase(@RequestBody List<MenuDto> selectedMenus) {
        userService.putMenuList(selectedMenus); //주문 들어온 메뉴들 sell테이블에 저장
        return "구매가 완료되었습니다.";
    } // Json 이용해서 웹페이지 주문내역을 서버로 가져옴
}
