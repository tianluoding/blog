package com.blog.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Test implements Serializable {
    private Long id;

    private String name;

    private String password;

}