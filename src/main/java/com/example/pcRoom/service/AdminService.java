package com.example.pcRoom.service;

import com.example.pcRoom.config.PrincipalDetails;
import com.example.pcRoom.dto.BestSellerDto;
import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.dto.UsersDto;
import com.example.pcRoom.entity.Menu;
import com.example.pcRoom.entity.Sell;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.repository.MenuRepository;
import com.example.pcRoom.repository.SellRepository;
import com.example.pcRoom.repository.UsersRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.ToIntFunction;

@Service
public class AdminService {
    private final SellRepository sellRepository;
    private final MenuRepository menuRepository;
    private final UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AdminService(SellRepository sellRepository, MenuRepository menuRepository, UsersRepository usersRepository) {
        this.sellRepository = sellRepository;
        this.menuRepository = menuRepository;
        this.usersRepository = usersRepository;
    }

    public List<SellDto> sell(Page<Sell> paging) {
        List<SellDto> sellDtoList = new ArrayList<>();

        for (Sell s : paging.getContent()) {
            Menu menu = menuRepository.findById(s.getMenuId()).orElse(null);
            Users users = usersRepository.findById(Long.valueOf(s.getUserNo())).orElse(null);

            MenuDto menuDto = MenuDto.fromMenuEntity(menu);
            UsersDto usersDto = UsersDto.fromUserEntity(users);

            int sellAmount = s.getSellAmount();
            sellDtoList.add(SellDto.fromSellEntity(s, menuDto, usersDto, sellAmount));
        }
        return sellDtoList;
    } // 주문 현황


    public List<SellDto> total() {
        List<SellDto> sellDtos =  new ArrayList<>();

        Integer total = sellRepository.total();

        sellDtos.add(SellDto.fromTotal(total));
        return sellDtos;

    } // 총 매출


    public Page<Sell> pagingList(Pageable pageable) {
        return sellRepository.findAll(pageable);
    } // 페이징된 데이터 반환
    

    public void minusSellAmountToMenuAmount(Long menuId , int sellAmount) throws Exception{
        Menu menu = menuRepository.findById(menuId).orElse(null);
        int menuAmount = menu.getMenuAmount();
        if (sellAmount > menuAmount) {
            throw new Exception(menu.getMenuName() + "의 재고가 없습니다.");
        }else{
            menuAmount -= sellAmount;
            menu.setMenuAmount(menuAmount);
            menuRepository.save(menu);
        }
    }//주문한 수량에 맞게 재고가 줄어들게 //sellAmount 주문한 수량 , menuAmount 재고 (주문한 수량 > 재고 일때 구현해야함 미완)

    public List<BestSellerDto> getBestSellers() {
        List<Object[]> results = sellRepository.getSalesSum();
        List<BestSellerDto> bestSellers = new ArrayList<>();

        // 쿼리 결과를 한 줄씩 돌리고 BestSellerDto 객체를 생성 후 리스트에 추가
        for (Object[] result : results) {

            // List 의 첫 번째 값을 menuId 로 변환
            Long menuId = ((Number) result[0]).longValue();
            // List 의 두 번째 값을 sumSellAmount 로 변환
            int sumSellAmount = ((Number) result[1]).intValue();

            // Menu 엔티티를 가져와서 MenuDto 로 변환
            Menu menu = menuRepository.findById(menuId).orElse(null);
            MenuDto menuDto = MenuDto.fromMenuEntity(menu);

            // 싹 추가
            bestSellers.add(BestSellerDto.fromBestEntity(menuId,sumSellAmount,menuDto));
        }

        // 판매 순위
        // bestSellers 리스트 돌리기
        for (int i = 0; i < bestSellers.size(); i++) {
            // rank 초기값 1위로 주기
            Integer rank = 1;

            // 현재 값과 다른 값들  비교
            for (int z = 0; z < bestSellers.size(); z++) {
                // 같은 값을 비교하지 않게 예외 처리
                if (i != z) {
                    // 현재의 판매량이 비교 대상보다 작으면 rank 값 증가
                    if (bestSellers.get(i).getSumSellAmount() < bestSellers.get(z).getSumSellAmount()) {
                        rank++;  // rank 값을 증가시켜 순위를 낮추기
                    }
                }
            }

            // 정리된 rank 값 -> bestSellers 리스트에 저장
            bestSellers.get(i).setRank(rank);
        }

        Collections.sort(bestSellers, Comparator.comparingInt(BestSellerDto::getRank));

        return bestSellers;

        //  Object[]는 주로 네이티브 쿼리 결과를 처리하거나, 여러 종류의 데이터를 함께 처리할 때 사용
        // 네이티브 쿼리에서 BestSellerDto 를 반환할 때 오류가 발생하면, Object[]로 결과를 받아서 수동으로 dto 로 변환

        // Collections.sort(bestSellers, Comparator.comparingInt(BestSellerDto::getRank));
        // 정렬 대상: bestSellers List
        // 정렬 기준: BestSellerDto 의 getRank() int(rank 타입이 int) 값
        // 오름차순 정렬: Comparator.comparingInt() -> 기본적으로 오름차순으로 정렬 됨
    } //제일 많이 나간 메뉴 가져오는 메소드

    public List<MenuDto> menuAll(Page<Menu> paging) {
        List<Menu> menuList = menuRepository.findAll();
        List<MenuDto> menuDtoList = new ArrayList<>();

        for(Menu menu : paging.getContent()){
            menuDtoList.add(MenuDto.fromMenuEntity(menu));
        }
        Collections.sort(menuDtoList, Comparator.comparingLong(MenuDto::getMenuId));
        return menuDtoList;
    } // 메뉴 전체를 보여주는 메소드

    public Page<Menu> menuPagingList(Pageable pageable) {
        return menuRepository.findAll(pageable);
    } // 메뉴 전체를 페이징 해주는 메소드


    public MenuDto updateView(Long menuId) {
        return menuRepository.findById(menuId) // ID로 메뉴 찾기
                .map(x -> MenuDto.fromMenuEntity(x)) // Entity -> dto
                .orElse(null);
    } //menu_id를 이용해서 메뉴정보를 찾는 메소드

    public void update(MenuDto menuDto) { // 메뉴 수정
        Menu menu = menuRepository.findById(menuDto.getMenuId()).orElse(null); // id로 메뉴찾기
        if (menu != null) {
            menu.setMenuPrice(menuDto.getMenuPrice()); // 메뉴 가격 수정 , 수정폼에서 입력한 가격을 menu 로

            // 기존 재고에 입력한 재고를 더해서 menu 로
            menu.setMenuAmount(menu.getMenuAmount() + menuDto.getMenuAmount());
            menuRepository.save(menu); // 저장
        }
    } //메뉴 수정 하는 메소드

    public UsersDto userUpdateView(Long userNo) {
        return usersRepository.findById(userNo)
                .map(x -> UsersDto.fromUserEntity(x))
                .orElse(null);
    } // 유저 번호를 이용해서 수정할 유저 정보 가져오는 메소드

    public void userUpdate(UsersDto usersDto) {
        String pw = passwordEncoder.encode(usersDto.getPassword()); //암호화된 비밀번호
        usersDto.setPassword(pw);
        Users users = usersDto.fromUserDto(usersDto);
        usersRepository.save(users);
    } //유저 정보를 수정하고 저장하는 메소드

    public void delete(Long userNo) {
        usersRepository.deleteById(userNo);
    } //유저번호를 이용해서 삭제하는 메소드



    public Page<UsersDto> search(String keyword, Pageable pageable) {
        Page<Users> page = usersRepository.findByUsernameContaining(keyword, pageable);
        return page.map(this::convertToDto);
    } // 키워드 이용해서 찾은 사용자 정보 페이징 출력

    public Page<UsersDto> usersPagingList(Pageable pageable) {
        Page<Users> page = usersRepository.findAll(pageable);
        return page.map(this::convertToDto);
    } // 사용자 정보 페이징 출력

    private UsersDto convertToDto(Users user) {
        Integer totalMoney = sellRepository.getTotalMoney(user.getUserNo());
        if (totalMoney == null) {
            totalMoney = 0; // 판매 기록이 없는 경우 총 금액을 0으로 설정
        }
        return new UsersDto(
                user.getUserNo(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getMoney(),
                user.getStatus(),
                totalMoney
        );
    }  //search(),usersPagingList()메소드에서 반환된 Users를 Dto 타입으로 변환


    public List<MenuDto> menuSearch(String type, String keyword) { // 메뉴 검색
        List<MenuDto> menuDtoList = new ArrayList<>();

        switch (type) {
            case "menuKind" :
                menuDtoList = menuRepository.searchMenuKind(keyword)
                        .stream()
                        .map(x -> MenuDto.fromMenuEntity(x))
                        .toList();
                break;
            case "menuName" :
                menuDtoList = menuRepository.searchMenuName(keyword)
                        .stream()
                        .map(x -> MenuDto.fromMenuEntity(x))
                        .toList();
                break;
        }
        return menuDtoList;
    } //메뉴 검색하는 메소드
}

