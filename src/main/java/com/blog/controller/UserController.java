package com.blog.controller;

import com.blog.domain.User;
import com.blog.resp.CommonResp;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public CommonResp<List<User>> list(){
        List<User> users = userService.list();
        return CommonResp.success(users);
    }

    @PostMapping("/save")
    public CommonResp<String> save(@RequestBody User user){
        userService.saveOrUpdate(user);
        return CommonResp.success("编辑成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<String> remove(@PathVariable String id) {
        userService.removeById(id);
        return CommonResp.success("删除成功");
    }
}
