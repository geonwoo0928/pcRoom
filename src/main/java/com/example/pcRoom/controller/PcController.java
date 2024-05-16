package com.example.pcRoom.controller;

import com.example.pcRoom.dto.*;
import com.example.pcRoom.entity.Sell;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.service.AdminService;
import com.example.pcRoom.service.PagingService;
import com.example.pcRoom.service.UserLoginRegisterService;
import com.example.pcRoom.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class PcController {
    private final AdminService adminService;
    private final UserService userService;
    private final PagingService pagingService;
    private final UserLoginRegisterService userLoginRegisterService;

    public PcController(AdminService adminService, UserService userService, PagingService pagingList, PagingService pagingService , UserLoginRegisterService userLoginRegisterService) {
        this.adminService = adminService;
        this.userService = userService;
        this.pagingService = pagingService;
        this.userLoginRegisterService = userLoginRegisterService;
    }

//    User

    @GetMapping("")
    public String userLoginView(){
        return "user/user_login";
    } // 로그인 화면

    @GetMapping("/user/signUp")
    public String userSingInView(Model model){
        return "user/user_signUp";
    }// 회원가입

    @GetMapping("/user")
    public String userMainPage() {

        return "user/user_main";
    } // 메인화면

    @GetMapping("/user/userMenu")
    public String userMenu(Model model) {
        List<MenuDto> menuDtoList = userService.showAllMenuKind("라면");
        model.addAttribute("menuDto", menuDtoList);
        return "/user/user_menu";
    } //[라면]메뉴판으로 이동

    @GetMapping("/user/userMenu2")
    public String userMenu2(Model model) {
        List<MenuDto> menuDtoList = userService.showAllMenuKind("음료");
        model.addAttribute("menuDto", menuDtoList);
        return "/user/user_menu_drink";
    } //[음료]메뉴판으로 이동


//    -----------------------admin------------------


    @GetMapping("/admin/users")
    public String usersList(@RequestParam(value = "keyword", required = false) String keyword,
                            @PageableDefault(page = 0, size = 10, sort = "userNo",
                                    direction = Sort.Direction.ASC) Pageable pageable,
                            Model model) {
        Page<Users> paging;

        if (keyword != null && !keyword.isEmpty()) {
            paging = adminService.search(keyword, pageable);
        } else {
            paging = adminService.usersPagingList(pageable);
        }

        int totalPage = paging.getTotalPages();
        List<Integer> barNumbers = pagingService.getPaginationBarNumbers(
                pageable.getPageNumber(), totalPage);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchList", paging.getContent()); // 페이지에서 컨텐트를 가져와야 함
        model.addAttribute("paging", paging);
        return "admin/user_list";
    }

    @GetMapping("/admin/sell")
    public String sell(Model model,
                       @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        // 페이징된 데이터 가져오기
        Page<Sell> paging = adminService.pagingList(pageable);

        // 현재 페이지의 SellDto 목록
        List<SellDto> sellDtoList = adminService.sell(paging);

        // 모델에 데이터 추가
        model.addAttribute("sellDto", sellDtoList);
        model.addAttribute("paging", paging);

        // 페이지네이션 바 정보 추가
        List<Integer> barNumbers = pagingService.pageNumbers(pageable.getPageNumber(), paging.getTotalPages());
        model.addAttribute("barNumbers", barNumbers);

        return "admin/sell";
    }

    @GetMapping("/admin/sales")
    public String sales(Model model) {
        List<BestSellerDto> bestSellers = adminService.getBestSellers();
        model.addAttribute("best", bestSellers);

        // 전체 매출
        List<SellDto> total = adminService.total();
        model.addAttribute("total", total);

        return "/admin/sales";
    }

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    } //로그인페이지 (첫화면)

    @GetMapping("/user/register")
    public String register(Model model){
        CreateUserDto createUserDto = new CreateUserDto();
        model.addAttribute(createUserDto);
        return "/user/register";
    } //회원가입페이지

    @PostMapping("/user/register")
    public String registerPost(@Valid @ModelAttribute("createUserDto") CreateUserDto createUserDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/signup";
        }

        if (!(createUserDto.getPassword1().equals(createUserDto.getPassword2()))) {
            bindingResult.rejectValue("password2", "password incorrect", "비밀번호가 일치하지 않습니다");
            return "signUp";
        }
        try {
            userLoginRegisterService.createUser(createUserDto);
        } catch (DataIntegrityViolationException e) { //동일한 사용자
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "signup";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup";
        }
        return "redirect:/user/login";
    }
    @GetMapping("/admin/menu")
    public String menuAll(Model model) {
        List<MenuDto> menuDtoList = adminService.menuAll();
        model.addAttribute("menuList", menuDtoList);
        return "/admin/menu";
    }

    @GetMapping("/admin/menuUpdate")
    public String updateView(@RequestParam("updateMenuId") Long menuId, Model model) {
        MenuDto menuDto = adminService.updateView(menuId);
        model.addAttribute("menuUpdate", menuDto);
        return "/admin/menuUpdate";
    }

    @PostMapping("/admin/menuUpdate")
    public String update(@ModelAttribute("menuUpdate") MenuDto menuDto) {
        adminService.update(menuDto);
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/userUpdate")
    public String userUpdateView(@RequestParam("updateUserNo") Long userNo, Model model) {
        UsersDto usersDto = adminService.userUpdateView(userNo);
        model.addAttribute("userUpdate", usersDto);
        return "/admin/userUpdate";
    }

    @PostMapping("/admin/userUpdate")
    public String userUpdate(@ModelAttribute("userUpdate") UsersDto usersDto) {
        adminService.userUpdate(usersDto);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/userDelete")
    public String userDelete(@RequestParam("delete") Long userNo) {
        adminService.delete(userNo);
        return "redirect:/admin/users";
    }
}
