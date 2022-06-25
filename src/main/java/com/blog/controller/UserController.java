package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.domain.User;
import com.blog.exception.BusinessException;
import com.blog.exception.BusinessExceptionCode;
import com.blog.req.UserLoginReq;
import com.blog.resp.CommonResp;
import com.blog.resp.UserLoginResp;
import com.blog.service.UserService;
import com.blog.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@RequestBody UserLoginReq req){
        User user = CopyUtil.copy(req, User.class);
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getLoginName, user.getLoginName());
        User user1 = userService.getOne(queryWrapper);
        if(user1 != null){
            // 用户名存在
            if(user1.getPassword().equals(user.getPassword())){
                //登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(user1, UserLoginResp.class);

                //token写入redis
                String token = UUID.randomUUID().toString().replaceAll("-", "");
                userLoginResp.setToken(token);

                redisTemplate.opsForValue().set(token, userLoginResp, 3600*24, TimeUnit.SECONDS);

                log.info("token is: {}", token);

                return CommonResp.success(userLoginResp);
            }else{
                //登录失败
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }else{
            // 用户名不存在
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }
    }

    @GetMapping("/logout/{token}")
    public CommonResp<String> logout(@PathVariable String token){
        redisTemplate.delete(token);
        log.info("从redis中删除token");
        return CommonResp.success("退出登录成功");
    }
}
