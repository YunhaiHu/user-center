package com.yunhai.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunhai.usercenter.model.domain.User;
import com.yunhai.usercenter.model.domain.request.UserLoginRequest;
import com.yunhai.usercenter.model.domain.request.UserRegisterRequest;
import com.yunhai.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yunhai.usercenter.contant.UserContant.ADMIN_ROLE;
import static com.yunhai.usercenter.contant.UserContant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author yunhai
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("/register")
    public Long userRegester(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            return null;
        }
        String userAccount=userRegisterRequest.getUserAccount();
        String userPassword=userRegisterRequest.getUserPassword();
        String checkPassword=userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            return null;
        }
        String userAccount=userLoginRequest.getUserAccount();
        String userPassword=userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }

        return userService.userLogin(userAccount,userPassword,request);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return safetyUser;
    }
    @GetMapping("/search")
    public List<User> searchUsers(String userName,HttpServletRequest request){
        //仅管理员可查询
        if (!isAdmin(request)){
            return  new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        if (StringUtils.isAnyBlank(userName)){
            queryWrapper.like("userName",userName);
        }
        List<User> userList = userService.list(queryWrapper);
        return  userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }


    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest request){
        //仅管理员可删除
        if (!isAdmin(request)){
            return  false;
        }
        if (id<=0){
            return  false;
        }
        return  userService.removeById(id);
    }
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User) userObj;
        return user != null && user.getUserRole()== ADMIN_ROLE;
    }
}
