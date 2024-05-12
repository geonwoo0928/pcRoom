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
    public PcController(AdminService adminService, UserService userService, PagingService pagingList, PagingService pagingService, UserLoginRegisterService userLoginRegisterService) {
        this.adminService = adminService;
        this.userService = userService;
        this.pagingService = pagingService;
        this.userLoginRegisterService = userLoginRegisterService;
    }

//    User

    @GetMapping("")
    public String userMainPage(){

        return "user/user_main";
    }

    @GetMapping("/user/userMenu")
    public String userMenu(Model model){
        List<MenuDto> menuDtoList = userService.showAllMenuKind("라면");
        model.addAttribute("menuDto" , menuDtoList);
        return "/user/user_menu";
    } //[라면]메뉴판으로 이동

    @GetMapping("/user/userMenu2")
    public String userMenu2(Model model){
        List<MenuDto> menuDtoList = userService.showAllMenuKind("음료");
        model.addAttribute("menuDto" , menuDtoList);
        return "/user/user_menu_drink";
    } //[음료]메뉴판으로 이동


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
    public String registerPost(@Valid @ModelAttribute("createUserDto") CreateUserDto createUserDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/signup";
        }

        if(!(createUserDto.getPassword1().equals(createUserDto.getPassword2()))){
            bindingResult.rejectValue("password2" , "password incorrect" ,"비밀번호가 일치하지 않습니다");
            return "signUp";
        }
        try{
            userLoginRegisterService.createUser(createUserDto);
        } catch(DataIntegrityViolationException e){ //동일한 사용자
            e.printStackTrace();
            bindingResult.reject("signupFailed" , "이미 등록된 사용자 입니다.");
            return "signup";
        }catch (Exception e){
            bindingResult.reject("signupFailed" , e.getMessage());
            return "signup";
        }
        return "redirect:/user/login";
    }
}
