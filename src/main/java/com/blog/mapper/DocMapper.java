package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.domain.Doc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DocMapper extends BaseMapper<Doc> {
}
