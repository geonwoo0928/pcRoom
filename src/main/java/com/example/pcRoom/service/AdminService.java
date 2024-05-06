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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<SellDto> sell() {
        List<Sell> sells = sellRepository.findAll(); // 모든 테이블 내용 가져오기
        List<SellDto> sellDto = new ArrayList<>(); // 담아줄 빈 껍데기

        for (Sell s : sells) {
            Menu menu = menuRepository.findById(Long.valueOf(s.getMenuId())).orElse(null);
            // orderForm 의 MenuId 랑 Menu 테이블의 MenuId 가 같으니까
            Users users = usersRepository.findById(s.getUserId()).orElse(null);
            MenuDto menuDto = MenuDto.fromMenuEntity(menu); // Entity 에서 필요한 정보 dto 로 가져오기?
            UsersDto usersDto = UsersDto.fromUserEntity(users);
            int sellAmount = s.getSellAmount();
            sellDto.add(SellDto.fromSellEntity(s ,menuDto, usersDto, sellAmount)); // 다 추가 ~
        }
        return sellDto;
    }

    public List<SellDto> total() {
        List<SellDto> sellDtos =  new ArrayList<>();

        int total = sellRepository.total();
        sellDtos.add(SellDto.fromtotal(total));
        return sellDtos;
    }
}
