package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.Setmeal;
import com.itheima.mapper.CategoryMapper;
import com.itheima.reggie.common.CustomeException;
import com.itheima.service.CategoryService;
import com.itheima.service.DishService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends
        ServiceImpl<CategoryMapper, Category>
        implements CategoryService {


    @Autowired
    private DishService dishService;     //菜品service

    @Autowired
    private SetmealService setmealService; //套餐service


    //依据分类id删除分类数据
    @Override
    public void remove(Long id) {
        //1:创建查询条件菜品(Dish)             10    14:50 完成任务
        LambdaQueryWrapper<Dish> lambdaQueryWrapper
                = new LambdaQueryWrapper<>();
        //2:如果dish分类id与我参数相同
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //3:查询count                        3 有3菜口与分类相关
        int count1 = dishService.count(lambdaQueryWrapper);
        //4:判断count > 0 抛出自定义异常对象
        if(count1 > 0){
            throw new CustomeException("当前分类下有关联，菜单不能删除");
        }
        //5:创建查询条件套餐(Setmeal)
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        //6:如果setemal中
        //7:catgoryId与参数 相同 10
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //8:查询count
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //9:如果count > 0 抛出自定义异常对象
        if(count2 > 0){
            throw  new CustomeException("当前分类关联套餐，不允许删除");
        }
        //10:删除当前分类
        super.removeById(id);
    }
}
