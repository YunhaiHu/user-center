package com.yunhai.usercenter.service;

import com.yunhai.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Shinelon
* @description 针对表【user】的数据库操作Service
* @createDate 2023-03-09 21:35:42
*/
public interface UserService extends IService<User> {

     String USER_LOGIN_STATE="userLoginState";
    /**
     *  用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 核对密码
     * @return 新用户ID
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @return 返回脱敏后的用户信息
     */

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */

    User getSafetyUser(User originUser);

    int userLogout(HttpServletRequest request);
}
