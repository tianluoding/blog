package com.blog.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLoginReq {

    @NotNull(message = "【用户名】不能为空")
    private String loginName;

    @NotNull(message = "【密码】不能为空")
    private String password;
}
