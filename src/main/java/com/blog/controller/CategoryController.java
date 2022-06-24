package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.Category;
import com.blog.resp.CommonResp;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public  CommonResp<Page> page(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
//        log.info("{}: {}", page, pageSize);
        //LambdaQueryWrapper<Ebook> queryWrapper = new LambdaQueryWrapper<>();
        categoryService.page(pageInfo);
        return CommonResp.success(pageInfo);
    }

    @PostMapping("/save")
    public CommonResp<String> save(@RequestBody Category category){
        categoryService.saveOrUpdate(category);
        return CommonResp.success("编辑分类成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<String> remove(@PathVariable String id){
        categoryService.removeById(id);
        return CommonResp.success("删除成功");
    }
}
