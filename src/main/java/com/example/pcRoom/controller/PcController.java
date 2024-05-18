package com.example.pcRoom.controller;

import com.example.pcRoom.config.PrincipalDetails;
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
        System.out.println(createUserDto.toString());

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

    @GetMapping("/user")
    public String userMainPage(Model model) {
        UsersDto currentUserDto = userService.showCurrentUser();
        model.addAttribute("currentUser", currentUserDto); // 현재 사용자 정보를 모델에 추가
        return "/user/user_main";
    } // 메인화면


    @GetMapping("/user/userSelfUpdate")
    public String showUpdateForm(HttpSession session, Model model) {
        // 세션에서 현재 사용자 정보를 가져오기
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
            return "redirect:/user/user_login";
        }
        // 수정 폼에 사용할 사용자 정보를 모델에 추가합니다.
        model.addAttribute("user", currentUser);
        return "/user/userSelfUpdate";
    }

    @PostMapping("/user/userSelfUpdate")
    public String userSelfUpdate(HttpSession session, @ModelAttribute("user") Users updatedUser){

        // 세션에서 현재 사용자 정보를 가져옵니다.
        Users currentUser = (Users) session.getAttribute("user");
        if (currentUser == null) {
            // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트합니다.
            return "redirect:/login";
        }
        // 수정된 정보로 사용자 정보를 업데이트합니다.
        currentUser.setName(updatedUser.getName());
        currentUser.setEmail(updatedUser.getEmail());
        // 나머지 필드도 업데이트하는 작업을 수행합니다.

        // 업데이트된 사용자 정보를 세션에 다시 저장합니다.
        session.setAttribute("user", currentUser);
        // 사용자 정보가 성공적으로 업데이트되었음을 나타내는 페이지로 리다이렉트합니다.
        return "redirect:/registrationSuccess";
    }


    @PostMapping("/user/userDelete")
    public String deleteUser(@RequestParam("deleteUserId") Long userNo) {
        adminService.delete(userNo); // adminService에서 사용자 삭제 로직을 처리
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
        // 가장 많이 판매한 메뉴
        List<BestSellerDto> bestSellers = adminService.getBestSellers();
        model.addAttribute("best", bestSellers);

        // 전체 매출
        List<SellDto> total = adminService.total();
        model.addAttribute("total", total);

        return "/admin/sales";
    }

    @GetMapping("/admin/menu")
    public String menuAll(Model model,
                          @RequestParam(value = "type", required = false) String type,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @PageableDefault(page = 0, size = 10, sort = "menuId", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Menu> paging = Page.empty(); // 비어있는 페이지
        List<MenuDto> menuDtoList;

        // 검색어가 있는 경우
        if (type != null && keyword != null && !keyword.isEmpty()) {
            menuDtoList = adminService.menuSearch(type, keyword);
        } else {
            // 검색어가 없는 경우
            paging = adminService.menuPagingList(pageable);
            menuDtoList = adminService.menuAll(paging);
        }

        // 모델에 데이터 추가
        model.addAttribute("menuList", menuDtoList);
        model.addAttribute("paging", paging);

        // 페이지네이션 바 정보 추가
        List<Integer> barNumbers = pagingService.pageNumbers(
                pageable.getPageNumber(),
                paging.getTotalPages());
        model.addAttribute("barNumbers", barNumbers);

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

    @GetMapping("/admin/userRank")
    public String userRank(Model model) {
        List<TotalMoneyDto> totalMoneyDtos = userService.totalMoney();
        model.addAttribute("totalMoney", totalMoneyDtos);

        return "admin/userRank";
    }

//    @GetMapping("/admin/search")
//    public String menuSearch(@RequestParam("type") String type,
//                             @RequestParam("keyword") String keyword,
//                             Model model) {
//        List<MenuDto> menuDtoList = adminService.menuSearch(type,keyword);
//        model.addAttribute("menuSearch", menuDtoList);
//
//        return "/admin/menu";
//    }
}
