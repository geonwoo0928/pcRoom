package com.example.pcRoom.repository;

import com.example.pcRoom.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Long> {
    Menu findBymenuName(String name);
}
