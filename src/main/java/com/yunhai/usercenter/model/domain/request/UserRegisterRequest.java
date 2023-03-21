package com.yunhai.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author yunhai
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -3817082911269816296L;
    private  String userAccount;
    private  String userPassword;
    private  String checkPassword;
}
