package com.example.pcRoom.controller;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> purchase(@RequestBody List<MenuDto> selectedMenus) {
        try {
            userService.putMenuList(selectedMenus);
            return ResponseEntity.ok().body("{\"message\":\"구매가 완료되었습니다.\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    } // Json 이용해서 웹페이지 주문내역을 서버로 가져옴
}
