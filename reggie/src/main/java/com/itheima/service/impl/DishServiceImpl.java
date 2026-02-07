package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.dto.DishDto;
import com.itheima.entity.Dish;
import com.itheima.entity.DishFlavor;
import com.itheima.mapper.DishMapper;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl
        extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    // 什么时候开启事务
    // 1: 如果同时操作一张以上（2，3，4...）
    // 2: 更新操作  insert update delete
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //1:保存菜品基本信息 name price image ...
        this.save(dishDto);
        //2:获取菜口id 雪花算法
        Long dishId = dishDto.getId();
        //3:获取所有口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        log.info("指定菜品口味 {}",flavors);
        //4:创建循环遍历所有口味，为每个口味添加菜品id
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //5:保存菜品口味数据到口味表 dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品与口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //1:依据id查询菜品
        Dish dish = this.getById(id);
        //2:创建dishdto
        DishDto dishDto = new DishDto();
        //3:复制对象属性dish -> dishdto
        BeanUtils.copyProperties(dish,dishDto);
        //4:查询当前菜口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        //5:将口味列表保存dishdto
        dishDto.setFlavors(list);
        //6:返回dto
        return dishDto;
    }

    /*
    * 更新菜品
    * 由于更新两张表，开启事务
    * @param disDto
    * */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //1.依据id更新菜品的数据（那么,price,...）
        this.updateById(dishDto);
        //2.创建查询对象，查询当前菜品所有口味
        LambdaQueryWrapper<DishFlavor> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //3.删除口味
        dishFlavorService.remove(queryWrapper);
        //4.获取新菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        //5.创建循环遍历口味 dish_id
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //6.批量保存口味
        dishFlavorService.saveBatch(flavors);
    }
}
