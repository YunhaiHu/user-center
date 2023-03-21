package com.yunhai.usercenter.service;

import com.yunhai.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testAddUser(){
        User user=new User();
        user.setUserName("YUNHAI");
        user.setUserAccount("123");
        user.setAvatarUrl("https://123.yunhai.club/img/avatar.jpg");
        user.setGender(0);
        user.setUserPassword("122");
        user.setEmail("122");
        user.setPhone("123");
        boolean result= userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);


    }

    @Test
    void userRegister() {

        String userAccount="yupi";
        String userPassword="123456";
        String checkPassword="123456";
        long result=userService.userRegister(userAccount,userPassword,checkPassword);
        Assertions.assertEquals(-1,result);



    }
}