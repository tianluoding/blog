package com.blog.controller;

import com.blog.domain.Content;
import com.blog.resp.CommonResp;
import com.blog.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    @GetMapping("/{id}")
    public CommonResp<Content> getContent(@PathVariable Long id){
        Content content = contentService.getById(id);

        return CommonResp.success(content);
    }

    @PostMapping
    public CommonResp<String> saveContent(@RequestBody Content content){
        contentService.saveOrUpdate(content);
        return CommonResp.success("保存成功");
    }
}
