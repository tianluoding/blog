package com.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@Slf4j
public class BlogApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(BlogApplication.class, args).getEnvironment();
        log.info("地址:{}:{}", environment.getProperty("server.address"), environment.getProperty("server.port"));
    }

}
