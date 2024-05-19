package com.example.pcRoom.controller;

import com.example.pcRoom.config.PrincipalDetails;
import com.example.pcRoom.constant.Status;
import com.example.pcRoom.dto.*;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.entity.Sell;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class PcAdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final PagingService pagingService;
    private final UserLoginRegisterService userLoginRegisterService;

    public PcAdminController(AdminService adminService, UserService userService, PagingService pagingList, PagingService pagingService , UserLoginRegisterService userLoginRegisterService) {
        this.adminService = adminService;
        this.userService = userService;
        this.pagingService = pagingService;
        this.userLoginRegisterService = userLoginRegisterService;
    }

    @GetMapping("/admin/users")
    public String usersList(@RequestParam(value = "keyword", required = false) String keyword,
                            @PageableDefault(page = 0, size = 10, sort = "userNo",
                                    direction = Sort.Direction.ASC) Pageable pageable,
                            Model model) {
        Page<UsersDto> dtoPaging;

        if (keyword != null && !keyword.isEmpty()) {
            dtoPaging = adminService.search(keyword, pageable);
        } else {
            dtoPaging = adminService.usersPagingList(pageable);
        }

        int totalPage = dtoPaging.getTotalPages();
        List<Integer> barNumbers = pagingService.getPaginationBarNumbers(
                pageable.getPageNumber(), totalPage);
        model.addAttribute("paginationBarNumbers", barNumbers);
        model.addAttribute("searchList", dtoPaging.getContent()); // 페이지에서 컨텐트를 가져와야 함
        model.addAttribute("paging", dtoPaging);

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
}
