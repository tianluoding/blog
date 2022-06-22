package com.blog.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ebook implements Serializable {
    private Long id;

    private String name;

    private Long category1Id;

    private Long category2Id;

    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;

}