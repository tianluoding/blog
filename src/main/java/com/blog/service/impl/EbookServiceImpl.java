package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.Ebook;
import com.blog.mapper.EbookMapper;
import com.blog.service.EbookService;
import org.springframework.stereotype.Service;

@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements EbookService {
}
