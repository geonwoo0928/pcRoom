package com.example.pcRoom.repository;

import com.example.pcRoom.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellRepository extends JpaRepository<Sell,Long> {
    @Query(value = "select sum(s.sell_amount * m.menu_price) from sell as s inner join menu as m on s.menu_id = m.menu_id" , nativeQuery = true)
    int total();
}
