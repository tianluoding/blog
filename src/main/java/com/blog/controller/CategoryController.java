package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.domain.Category;
import com.blog.req.PageReq;
import com.blog.resp.CommonResp;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public  CommonResp<Page> page(@Valid PageReq pageReq) {
        Page pageInfo = new Page(pageReq.getPage(), pageReq.getPageSize());
        categoryService.page(pageInfo);
        return CommonResp.success(pageInfo);
    }

    @GetMapping("/list")
    public CommonResp<List<Category>> list(){
        List<Category> categories = categoryService.list();
        return CommonResp.success(categories);
    }

    @PostMapping("/save")
    public CommonResp<String> save(@RequestBody @Valid Category category){
        categoryService.saveOrUpdate(category);
        return CommonResp.success("编辑分类成功");
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<String> remove(@PathVariable String id){
        categoryService.removeById(id);
        return CommonResp.success("删除成功");
    }
}
