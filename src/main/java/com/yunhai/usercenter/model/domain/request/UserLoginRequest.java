package com.yunhai.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -4109467728558637346L;

    private String userAccount;
    private String userPassword;

}
