package com.example.pcRoom.controller;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.service.AdminService;
import com.example.pcRoom.service.PagingService;
import com.example.pcRoom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class PcController {
    private final AdminService adminService;
    private final UserService userService;
    private final PagingService pagingService;
    public PcController(AdminService adminService, UserService userService, PagingService pagingList, PagingService pagingService) {
        this.adminService = adminService;
        this.userService = userService;
        this.pagingService = pagingService;
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

    @PostMapping("/user/userMenu")
    public String userMenuPost(@RequestParam("menuName") String menuName){
        log.info(menuName);
        return "/user/user_menu";
    }



//    -----------------------admin------------------

//    @GetMapping("/admin/users")
//    public String adminMain(Model model) {
//        List<UsersDto> usersDtoList = userService.usersList();
//        model.addAttribute("usersDto", usersDtoList);
//        return "admin/user_list";
//    }

    @GetMapping("/admin/users")
    public String testView(Model model,
                           @PageableDefault(page = 0, size = 10, sort = "userId",
                                   direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Users> paging = userService.pagingList(pageable);

        int totalPage = paging.getTotalPages();
        List<Integer> barNumbers = pagingService.getPaginationBarNumbers(
                pageable.getPageNumber(), totalPage);
        model.addAttribute("paginationBarNumbers", barNumbers);

        model.addAttribute("paging", paging);
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
