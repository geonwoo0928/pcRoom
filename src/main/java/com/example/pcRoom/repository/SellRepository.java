package com.example.pcRoom.repository;

import com.example.pcRoom.dto.BestSellerDto;
import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell,Long> {
    @Query(value = "select sum(s.sell_amount * m.menu_price) from sell as s inner join menu as m on s.menu_id = m.menu_id" , nativeQuery = true)
    Integer total(); // 총 매출

    @Query(value = "SELECT s.user_no as userNo, COALESCE(SUM(s.sell_amount * m.menu_price), 0) as totalMoney from sell as s inner join menu as m on s.menu_id = m.menu_id " +
            "group by s.user_no", nativeQuery = true)
    List<Object[]> totalMoney(); // 사용자 총 사용 금액

    @Query(value = "select menu_id as menuId, sum(sell_amount) as sumSellAmount from sell group by menu_id" , nativeQuery = true)
    List<Object[]> getSalesSum(); // 베스트 셀러 찾기
}
