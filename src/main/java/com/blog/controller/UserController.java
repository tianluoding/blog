package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.domain.User;
import com.blog.exception.BusinessException;
import com.blog.exception.BusinessExceptionCode;
import com.blog.resp.CommonResp;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        if(user.getId() == null){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getLoginName, user.getLoginName());
            User user1 = userService.getOne(queryWrapper);
            if(user1 == null){
                userService.save(user);
            }else{
                // 用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            // 更新用户名不允许修改
            user.setLoginName(null);
            user.setPassword(null);
            userService.updateById(user);
        }

        return CommonResp.success("编辑成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<String> remove(@PathVariable String id) {
        userService.removeById(id);
        return CommonResp.success("删除成功");
    }
}
