package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.Category;
import com.itheima.reggie.common.Constants;
import com.itheima.reggie.common.R;
import com.itheima.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //参数 type=1 菜品分类  type=2套餐分类
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //添加条件构造器
        LambdaQueryWrapper<Category> queryWrapper =
                new LambdaQueryWrapper<>();
        //添加条件type
        queryWrapper.eq(category.getType() != null,
                Category::getType,category.getType());
        //添加排序条件
        queryWrapper
                .orderByAsc(
                        Category::getSort)
                .orderByAsc(
                        Category::getUpdateTime
                );
        //查询
        List<Category> list = categoryService.list(queryWrapper);
        //返回结果   15:15 休息一下
        return R.success(list);
    }


    /*
    * 新增分类
    * 1.请求方式 post
    * 2.请求地址  /category
    * 3.参数  json category(name;sort;type)
    * 4.返回结果 添加成功
    * */
    @PostMapping
    public R<String> save(@RequestBody Category  category){
        //记录日志宝贵 调整 场景复现
        log.info("添加类别 {}",category.toString());
        categoryService.save(category);
        return R.success(Constants.MESSAGE_ADD_CATEGORY_OK);

    }
    /*
    * 分类，分页
    * 1.请求方式 get
    * 2.请求地址  /category/page
    * 3.参数  json page pageSize
    * 4.返回结果 [category{id,name,...}],total,pageCount,
    * */
    @GetMapping("/page")
    public R<Page> page(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "10")
            int pageSize){
        //1.创建分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //2.创建查询条件 升序
        LambdaQueryWrapper<Category> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        //3.分页查询
        categoryService.page(pageInfo,queryWrapper);
        //4.返回结果
        return R.success(pageInfo);
    }
    /*
    * 删除分类
    * 1.请求方式 delete
    * 2.请求地址  /category
    * 3.参数  id
    * 4.返回结果 "分类信息删除成功"
    * */
    @DeleteMapping
    public R<String> delete(long id){
        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /*
     * 依据id 更新类别
     * 1.请求方式 put
     * 2.请求地址  /category
     * 3.参数  category json
     * 4.返回结果 更新成功
     * */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类 {}",category.toString());
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

}
