package com.example.pcRoom.repository;

import com.example.pcRoom.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    Menu findBymenuName(String name);

    List<Menu> findBymenuKind(String kind);

    // 종류로 검색
    @Query(value = "select * from menu where menu_kind like %:keyword% order by menu_id" , nativeQuery = true)
    List<Menu> searchMenuKind(@Param("keyword") String keyword);

    // 이름으로 검색
    @Query(value = "select * from menu where menu_name like %:keyword% order by menu_id" , nativeQuery = true)
    List<Menu> searchMenuName(@Param("keyword") String keyword);
}
