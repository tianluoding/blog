package com.blog.service.impl;

import com.blog.domain.Test;
import com.blog.mapper.TestMapper;
import com.blog.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    public Test selectById(Long id) {
        return testMapper.selectByPrimaryKey(id);
    }
}
