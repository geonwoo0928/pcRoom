package com.example.pcRoom.controller;

import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.service.AdminService;
import com.example.pcRoom.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PcController {
    private final AdminService adminService;
    private final UserService userService;
    public PcController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("")
    public String userMainPage(){

        return "user/user_main";
    }

    @GetMapping("/main/sell")
    public String sell(Model model) {
        List<SellDto> sellDtoList = adminService.sell();
        model.addAttribute("sellDto", sellDtoList);
        return null; // 아직 페이지를 안 만들었음
    }
}
