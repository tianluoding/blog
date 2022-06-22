package com.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@ComponentScan("com.blog")
@MapperScan("com.blog.mapper")
@Slf4j
public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(BlogApplication.class, args).getEnvironment();
        log.info("地址:{}:{}", environment.getProperty("server.address"), environment.getProperty("server.port"));
    }

}
