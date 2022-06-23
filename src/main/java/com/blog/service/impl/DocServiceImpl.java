package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Doc;
import com.blog.mapper.DocMapper;
import com.blog.service.DocService;
import org.springframework.stereotype.Service;

@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {
}
