package com.example.pcRoom.service;

import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Integer> menuFrequencyMap = new HashMap<>();
        String word = null;
        for (MenuDto menuDto : selectedMenus) {
            String menuName = menuDto.getMenuName();
            menuFrequencyMap.put(menuName, menuFrequencyMap.getOrDefault(menuName, 0) + 1);
        } //메뉴이름 추출 , 빈도 계산

        for (Map.Entry<String, Integer> entry : menuFrequencyMap.entrySet()) {
            String menuName = entry.getKey();
            int frequency = entry.getValue();
            if(frequency != 0){ //빈도수가 0이 아닌 메뉴만
                Menu menu = menuRepository.findBymenuName(menuName); //메뉴이름에 맞는 메뉴데이터 가져옴
                Long menuId= menu.getMenuId(); // 메뉴이름에 맞는 메뉴아이디 가져옴
                adminService.minusSellAmountToMenuAmount(menuId , frequency);
                if(menu.getMenuAmount() > frequency){
                    Sell sell = new Sell(menuId , frequency);
                    sellRepository.save(sell);
                } //주문할 수량 > 재고 일때 작동안되게끔
            }
            // 메뉴이름 , 빈도 저장
        }
    } // 주문 들어온 메뉴들 db에 저장



    public Page<Users> pagingList(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }
}
