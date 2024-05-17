package com.example.pcRoom.repository;

import com.example.pcRoom.dto.BestSellerDto;
import com.example.pcRoom.dto.MenuDto;
import com.example.pcRoom.dto.SellDto;
import com.example.pcRoom.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell,Long> {
    @Query(value = "select sum(s.sell_amount * m.menu_price) from sell as s inner join menu as m on s.menu_id = m.menu_id" , nativeQuery = true)
    Integer total(); // 총 매출

    @Query(value = "select menu_id as menuId, sum(sell_amount) as sumSellAmount from sell group by menu_id" , nativeQuery = true)
    List<Object[]> getSalesSum(); // 베스트 셀러 찾기

    @Query(value = "SELECT SUM(m.menu_price * s.sell_amount) " +
            "FROM sell s " +
            "JOIN menu m ON s.menu_id = m.menu_id " +
            "WHERE s.user_no = :userNo " +
            "GROUP BY s.user_no", nativeQuery = true)
    Integer getTotalMoney(@Param("userNo") Long userNo);
}
