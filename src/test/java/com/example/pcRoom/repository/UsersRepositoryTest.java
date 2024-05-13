package com.example.pcRoom.repository;

import com.example.pcRoom.entity.Users;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;
    @Test
    void findByUsername() {
        Optional<Users> user = usersRepository.findByUsername("ozllzlu");
        System.out.println(user.toString());
    }
}