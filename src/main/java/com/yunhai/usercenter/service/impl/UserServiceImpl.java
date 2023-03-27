package com.yunhai.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunhai.usercenter.model.domain.User;
import com.yunhai.usercenter.service.UserService;
import com.yunhai.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Shinelon
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-03-09 21:35:42
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT="yupi";
    /**
     * 用户登录态键
     */



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if (userAccount.length()<4){
            return  -1;
        }
        if(userPassword.length()<6 || checkPassword.length()<6){
            return  -1;
        }
        //校验账户包含特殊字符
        String validPattern = ".*[*?!&￥$%^#,./@\";:><\\]\\[}{\\-=+_\\\\|》《。，、？’‘“”~`）].*$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return  -1;
        }
        if (!userPassword.equals(checkPassword)){
            return -1;
        }
        //账户不能复现
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count>0){
            return -1;
        }
        //2.加密

        String encryptPassword=DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3.插入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult){
            return  -1;
        }

        return  user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if (userAccount.length()<4){
            return  null;
        }
        if(userPassword.length()<6 ){
            return  null;
        }
        //校验账户包含特殊字符
        String validPattern = ".*[*?!&￥$%^#,./@\";:><\\]\\[}{\\-=+_\\\\|》《。，、？’‘“”~`）].*$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            return  null;
        }
        //2.加密

        String encryptPassword=DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //账户不能复现
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }
        User safetyUser = getSafetyUser(user);
        //4.记录用户的状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null){
            return null;
        }
        //3.用户脱敏
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;

    }

}


















