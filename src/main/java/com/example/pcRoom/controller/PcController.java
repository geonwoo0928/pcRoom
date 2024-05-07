package com.example.pcRoom.controller;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.service.AdminService;
import com.example.pcRoom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class PcController {
    private final AdminService adminService;
    private final UserService userService;
    public PcController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

//    User

    @GetMapping("")
    public String userMainPage(){

        return "user/user_main";
    }


    @GetMapping("/user/userMenu")
    public String userMenu(Model model){
        List<MenuDto> menuDtoList = userService.showAllMenu();
        model.addAttribute("menuDto" , menuDtoList);
        return "/user/user_menu";
    } //메뉴판으로 이동



//    admin

    @GetMapping("/admin/users")
    public String adminMain(Model model) {
        List<UsersDto> usersDtoList = userService.usersList();
        model.addAttribute("usersDto", usersDtoList);
        return "admin/user_list";
    }

    @GetMapping("/admin/sell")
    public String sell(Model model) {
        List<SellDto> sellDtoList = adminService.sell();
        List<SellDto> total = adminService.total();
        model.addAttribute("sellDto", sellDtoList);
        model.addAttribute("total", total);
        return "admin/sell";
    }

}
