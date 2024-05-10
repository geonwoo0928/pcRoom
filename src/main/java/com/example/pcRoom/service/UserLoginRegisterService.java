package com.example.pcRoom.service;

import com.example.pcRoom.constant.Status;
import com.example.pcRoom.dto.CreateUserDto;
import com.example.pcRoom.entity.Users;
import com.example.pcRoom.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginRegisterService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void createUser(CreateUserDto createUserDto){
        Users users = new Users();
        users.setUserId(createUserDto.getUserId());
        users.setName(createUserDto.getName());
        users.setPassword(passwordEncoder.encode(createUserDto.getPassword1()));
        users.setEmail(createUserDto.getEmail());
        users.setPhoneNum(createUserDto.getPhoneNum());
        if(createUserDto.getConfirmedNum() == 23028){
            users.setStatus(Status.ADMIN);
        }else{
            users.setStatus(Status.USER);
        }

        usersRepository.save(users);
    } //회원가입 서비스
}
