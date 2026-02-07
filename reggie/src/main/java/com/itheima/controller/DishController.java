package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.dto.DishDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.reggie.common.R;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    //自动装配，类别service
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("菜品添加dto {}", dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功");
    }

    /*
     * @param page 页码1 2 3
     * @param pageSize 页大小
     * @param name 模糊查询名称
     */
    @GetMapping("/page")
    public R<Page> page(
            @RequestParam(required = false, defaultValue = "1")
                    int page,
            @RequestParam(required = false, defaultValue = "10")
                    int pageSize,
            String name) {
        //任务一：基础 创建分页构造器对象    菜品page  dtopage
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //任务二：查询菜单基本信息(id,price,name,status)
        LambdaQueryWrapper<Dish> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),
                Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        //任务三: 将菜品分页转DishDto分页(类别名称)
        //1:复制对象 菜品基本信息   dto对象  菜品基本信息拷贝到dto对象
        BeanUtils.copyProperties(pageInfo,
                dishDtoPage, "records");
        //2:获取菜品page中records属性值(数组--[菜品1，菜品2,..])
        List<Dish> records = pageInfo.getRecords();
        //3:创建循环遍历数组
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        //3.1:获取类别id
        //3.2:依据类别id查询类别名称
        //3.3:如果不为空，将类别名称保存dto
        // 3.4:返回dto page
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 作用:回显菜品(口味) name:新辣鱼 price:99 [{辣度}]
     * 地址。  http://127.0.0.1:8080/dish/10191
     * 请求参数 {id}
     * 返回结果 DishDto
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("更新", dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }

    /**
     * 依据分类id查询菜单
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        //1:创建查询条件
        LambdaQueryWrapper<Dish> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,
                Dish::getCategoryId, dish.getCategoryId());
        //2:查询是起售状态菜品
        queryWrapper.eq(Dish::getStatus, 1);
        //3:排序
        queryWrapper.orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        //4:查询返回
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }

}


