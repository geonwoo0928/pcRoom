package com.example.pcRoom.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminService {
    private final SellRepository sellRepository;
    private final MenuRepository menuRepository;
    private final UsersRepository usersRepository;

    public AdminService(SellRepository sellRepository, MenuRepository menuRepository, UsersRepository usersRepository) {
        this.sellRepository = sellRepository;
        this.menuRepository = menuRepository;
        this.usersRepository = usersRepository;
    }

    public List<SellDto> sell(Page<Sell> paging) {
        List<SellDto> sellDtoList = new ArrayList<>();

        for (Sell s : paging.getContent()) {
            Menu menu = menuRepository.findById(s.getMenuId()).orElse(null);
            Users users = usersRepository.findById(s.getUserId()).orElse(null);

            MenuDto menuDto = MenuDto.fromMenuEntity(menu);
            UsersDto usersDto = UsersDto.fromUserEntity(users);

            int sellAmount = s.getSellAmount();
            sellDtoList.add(SellDto.fromSellEntity(s, menuDto, usersDto, sellAmount));
        }
        return sellDtoList;
    } // 주문 현황


    public List<SellDto> total() {
        List<SellDto> sellDtos =  new ArrayList<>();

        int total = sellRepository.total();
        sellDtos.add(SellDto.fromtotal(total));
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
            bestSellers.add(BestSellerDto.fromBestEntity(menuId, sumSellAmount,menuDto));
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
        // 정렬 기준: BestSellerDto 의 getRank() int 값
        // 오름차순 정렬: Comparator.comparingInt() -> 기본적으로 오름차순으로 정렬 됨
    }
}

