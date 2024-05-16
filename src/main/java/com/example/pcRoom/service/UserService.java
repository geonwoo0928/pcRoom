package com.example.pcRoom.service;

import com.example.pcRoom.config.PrincipalDetails;
import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.TotalMoneyDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.entity.Sell;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.repository.MenuRepository;
import com.example.pcRoom.repository.SellRepository;
import com.example.pcRoom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    SellRepository sellRepository;
    @Autowired
    AdminService adminService;

    public List<MenuDto> showAllMenuKind(String kind) {
        List<Menu> menuList = menuRepository.findBymenuKind(kind);
        List<MenuDto> menuDtoList = new ArrayList<>();
        for(Menu menu : menuList){
            menuDtoList.add(
                    MenuDto.fromMenuEntity(menu)
            );
        }
        return menuDtoList;
    } // 메뉴 전체를 가져오는 메소드

    public List<UsersDto> usersList(){
        List<UsersDto> usersDto = new ArrayList<>();
        return usersRepository.findAll()
                .stream()
                .map(x -> UsersDto.fromUserEntity(x))
                .toList();
    } // 모든 회원 정보 출력

    public void putMenuList(List<MenuDto> selectedMenus) throws Exception {

        //PrincipalDetails 에서 유저아이디 가져오는 코드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = null;
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            currentUserName = principalDetails.getUsername();
        }
        //PrincipalDetails 에서 유저아이디 가져오는 코드

        Optional<Users> userOptional = usersRepository.findByUsername(currentUserName);
        if (!userOptional.isPresent()) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
        Users user = userOptional.get();
        Long userNo = user.getUserNo();
        int money = user.getMoney(); //회원 잔액

        for (MenuDto menuDto : selectedMenus) {
            String menuName = menuDto.getMenuName();
            int quantity = menuDto.getQuantity();
            Menu menu = menuRepository.findBymenuName(menuName); //메뉴이름에 맞는 메뉴데이터 가져옴
            Long menuId = menu.getMenuId(); // 메뉴이름에 맞는 메뉴아이디 가져옴
            if (menu.getMenuAmount() >= quantity && money >= (quantity * menu.getMenuPrice())) { // 재고 >= 주문할 수량 일때만
                money = money - (quantity * menu.getMenuPrice()); //현재잔액 - 주문금액
                user.setMoney(money); // 현재잔액 - 주문금액 entity에 저장
                usersRepository.save(user); //db에 저장
                adminService.minusSellAmountToMenuAmount(menuId, quantity);
                Sell sell = new Sell(menuId, quantity, userNo);
                sellRepository.save(sell);
            }
        } // 메뉴이름 추출 , 빈도 계산
    }

    public Page<Users> pagingList(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public List<TotalMoneyDto> totalMoney() {
        List<Object[]> list = sellRepository.totalMoney();
        List<TotalMoneyDto> totalMoneyDtos = new ArrayList<>();

        for (Object[] result : list) {
            Long userNo = ((Number) result[0]).longValue();
            int totalMoney = ((Number) result[1]).intValue();

            Users users = usersRepository.findById(userNo).orElse(null);
            UsersDto usersDto = UsersDto.fromUserEntity(users);

            totalMoneyDtos.add(TotalMoneyDto.fromTotalMoney(userNo,totalMoney,usersDto));
        }
        // 회원별 구매 순위
        for (int i = 0; i < totalMoneyDtos.size(); i++) {
            Integer rank = 1;

            for (int z = 0; z < totalMoneyDtos.size(); z++){
                if (i != z) {
                    if (totalMoneyDtos.get(i).getTotalMoney() < totalMoneyDtos.get(z).getTotalMoney()) {
                        rank++;
                    }
                }
            }
            totalMoneyDtos.get(i).setRank(rank);
        }
        Collections.sort(totalMoneyDtos, Comparator.comparingInt(TotalMoneyDto::getRank));

        return totalMoneyDtos;
    }

}
