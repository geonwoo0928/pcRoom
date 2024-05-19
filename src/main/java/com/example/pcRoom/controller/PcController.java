package com.example.pcRoom.controller;

import com.example.pcRoom.config.PrincipalDetails;
import com.example.pcRoom.constant.Status;
import com.example.pcRoom.dto.*;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.entity.Sell;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.service.AdminService;
import com.example.pcRoom.service.PagingService;
import com.example.pcRoom.service.UserLoginRegisterService;
import com.example.pcRoom.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping("/user/login")
    public String userLoginView(){
        return "/user/user_login";
    } // 로그인 화면

    @GetMapping("/user/userSignUp")
    public String userSignInView(Model model){
        CreateUserDto createUserDto = new CreateUserDto();
        model.addAttribute("dto" , createUserDto);
        return "/user/userSignUp";
    }// 회원가입

    @PostMapping("/user/userSignUp")
    public String userSignPost(@Valid @ModelAttribute("dto") CreateUserDto createUserDto ,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "/user/userSignUp";
        }

        if (!(createUserDto.getPassword1().equals(createUserDto.getPassword2()))) {
            bindingResult.rejectValue("password2", "password incorrect", "비밀번호가 일치하지 않습니다");
            return "/user/userSignUp";
        }

        if(userService.userIdExist(createUserDto.getUserId())){
            bindingResult.rejectValue("userId" , "userId exist" , "사용자 ID가 존재합니다.");
            return "/user/userSignUp";
        }
        try {
            userLoginRegisterService.createUser(createUserDto);
        } catch (DataIntegrityViolationException e) { //동일한 사용자
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "/user/userSignUp";
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "/user/userSignUp";
        }
        return "redirect:/registrationSuccess";
    }

    @GetMapping("/registrationSuccess")
    public String showRegistrationSuccess(){
        return "/registrationSuccess";
    } //회원가입 성공페이지

    @GetMapping("/updateSuccess")
    public String showUpdateSuccess(){
        return "/updateSuccess";
    }

    @GetMapping("/user")
    public String userMainPage(Model model) {
        UsersDto currentUserDto = userService.showCurrentUser();
        Status status = currentUserDto.getStatus();

        if (status == status.ADMIN) {
            return "redirect:/admin/sell";
        }
        model.addAttribute("currentUser", currentUserDto); // 현재 사용자 정보를 모델에 추가
        return "/user/user_main";
    } // 메인화면

    @GetMapping("/admin/user")
    public String adminUser(Model model){
        UsersDto currentUserDto = userService.showCurrentUser();
        model.addAttribute("currentUser" , currentUserDto);
        return "/user/user_main";
    } // 관리자페이지 -> 유저페이지


    @GetMapping("/user/userSelfUpdate")
    public String showUpdateForm(Model model) {

        // userNo를 사용하여 해당 사용자의 정보를 가져온다
        UsersDto currentUserDto = userService.showCurrentUser(); // 현재 사용자 정보를 가져옴
        UpdateUserDto updateUserDto = new UpdateUserDto(); // 수정할 사용자 정보 DTO 생성

        // 현재 사용자의 정보를 updateUserDto에 복사
        updateUserDto.setUserNo(currentUserDto.getUserNo());
        updateUserDto.setUserId(currentUserDto.getUserId());
        updateUserDto.setName(currentUserDto.getName());
        model.addAttribute("updateUserDto", updateUserDto); // 모델에 추가
        return "/user/userSelfUpdate";
    }

    @PostMapping("/user/userSelfUpdate/{userNo}")
    public String userSelfUpdate(@ModelAttribute("updateUserDto") @Valid UpdateUserDto updateUserDto,
                                 BindingResult bindingResult) {
        // 유효성 검사 결과 확인
        if (bindingResult.hasErrors()) {
            // 에러가 있을 경우 수정 폼으로 다시 이동
            return "/user/userSelfUpdate";
        }

        if (!(updateUserDto.getPassword1().equals(updateUserDto.getPassword2()))) {
            bindingResult.rejectValue("password2", "password incorrect", "비밀번호가 일치하지 않습니다");
            return "/user/userSelfUpdate";
        }
        try {
            userService.updateUser(updateUserDto);
        }
         catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return "/user/userSelfUpdate";
        }



        return "redirect:/updateSuccess";
    }// 수정된 사용자 정보 업데이트

    @PostMapping("/user/userDelete")
    public String deleteUser() {
        userService.deleteUser(); // adminService에서 사용자 삭제 로직을 처리
        return "/user/user_login"; // 사용자 삭제 후 로그인 페이지로
    } // 로그인 한 해당 회원 계정 탈퇴

    @GetMapping("/user/userMenu")
    public String userMenu(Model model) {
        UsersDto usersDto = userService.showCurrentUser();
        int currentMoney = usersDto.getMoney();
        List<MenuDto> menuDtoList = userService.showAllMenuKind("라면");
        model.addAttribute("menuDto", menuDtoList);
        model.addAttribute("currentMoney" , currentMoney);
        return "/user/user_menu";
    } //[라면]메뉴판으로 이동

    @GetMapping("/user/userMenu2")
    public String userMenu2(Model model) {
        UsersDto usersDto = userService.showCurrentUser();
        int currentMoney = usersDto.getMoney();
        List<MenuDto> menuDtoList = userService.showAllMenuKind("음료");
        model.addAttribute("menuDto", menuDtoList);
        model.addAttribute("currentMoney" , currentMoney);
        return "/user/user_menu_drink";
    } //[음료]메뉴판으로 이동

    @GetMapping("/user/userMenu3")
    public String userMenu3(Model model) {
        UsersDto usersDto = userService.showCurrentUser();
        int currentMoney = usersDto.getMoney();
        List<MenuDto> menuDtoList = userService.showAllMenuKind("간식");
        model.addAttribute("menuDto", menuDtoList);
        model.addAttribute("currentMoney" , currentMoney);
        return "/user/user_menu_snack";
    } //[간식]메뉴판으로 이동

    @GetMapping("/user/userMenu4")
    public String userMenu4(Model model) {
        UsersDto usersDto = userService.showCurrentUser();
        int currentMoney = usersDto.getMoney();
        List<MenuDto> menuDtoList = userService.showAllMenuKind("과자");
        model.addAttribute("menuDto", menuDtoList);
        model.addAttribute("currentMoney" , currentMoney);
        return "/user/user_menu_chips";
    } //[과자]메뉴판으로 이동

    @GetMapping("/user/userInsertCoin")
    public String userInsertCoin(Model model) {
        UsersDto usersDto = userService.showCurrentUser();
        int currentMoney = usersDto.getMoney();
        model.addAttribute("currentMoney" , currentMoney);
        return "user/user_charge";
    }

    @PostMapping("/user/userInsertCoin")
    public String chargedCoin(@RequestParam("amount") int amount){
        userService.chargedCoin(amount);
        return "redirect:/user/userInsertCoin";
    } //금액충전
}