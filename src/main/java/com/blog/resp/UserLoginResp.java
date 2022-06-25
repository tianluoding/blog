package com.blog.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginResp implements Serializable {

    private Long id;

    private String name;

    private String token;

}
