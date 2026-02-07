package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.DishDto;
import com.itheima.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据
    // 需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //查询菜品id对应菜品与口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品
    //由于更新两张表 dish、dish_flavor
    public void updateWithFlavor(DishDto dishDto);
}