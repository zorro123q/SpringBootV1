package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;
import com.itheima.entity.SetmealDish;
import com.itheima.mapper.SetmealMapper;
import com.itheima.reggie.common.Constants;
import com.itheima.reggie.common.CustomeException;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl
        extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时需要保存套餐和套餐与菜品关联关系
     *         setmeal 表   setmeal_dish 表
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //1:保存套餐基本信息 name price
        this.save(setmealDto);
        //2:获取套餐对应菜品
        List<SetmealDish> setmealDishes =
                setmealDto.getSetmealDishes();
        //3:创建循环遍历菜品（赋值套餐id）
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //4:批量保存
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐
     * 1:删除套餐
     * 2:套餐关联菜品（删除关系表数据） 菜品不动
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where status = 1 and id in(10,20);
        //》 0 在售套售不能删除条件(在售)
        //1:任务一：是否可以删除套餐(如果在售商品不能删除) status==1
        LambdaQueryWrapper<Setmeal> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus, Constants.SETMEALL_ONLINE);
        int count = this.count(queryWrapper);
        if(count > 0){
            throw new CustomeException("套餐正在售卖中，不能删除");
        }
        //2:先删除关联关系表数据
        LambdaQueryWrapper<SetmealDish> queryWrapper2 =
                new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper2);
        //3:再删除自己套餐
        this.removeByIds(ids);

    }
}