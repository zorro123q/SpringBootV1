package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.entity.ShoppingCart;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据 {}", shoppingCart);

        //1:获取当前登录用户id,设置购物车对象  15:20 完成任务
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //2:创建查询对象
        LambdaQueryWrapper<ShoppingCart> queryWrapper =
                new LambdaQueryWrapper<>();
        //3:查询当前登录用户购物车
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        //4:如果有dishId 查询菜口
        queryWrapper.eq(shoppingCart.getDishId() != null,
                ShoppingCart::getDishId, shoppingCart.getDishId());
        //5:如果有setmealId 查询套餐
        queryWrapper.eq(shoppingCart.getSetmealId() != null,
                ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        //6:查询购物车满足条件对象
        ShoppingCart shopCart = shoppingCartService.getOne(queryWrapper);
        //7:如果查询结果(买过)
        //7.1:数量为1保存购物车
        if (shopCart != null) {
            Integer number = shopCart.getNumber();
            shopCart.setNumber(number + 1);
            shoppingCartService.updateById(shopCart);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shopCart = shoppingCart;
        }

        //8:如果买过
        //8.1:获取原有数量，数量加1 更新
        return R.success(shopCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        //查询当前登录用户的购物车xx
        //userId
        LambdaQueryWrapper<ShoppingCart> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list
                = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据 {}", shoppingCart);

        //1:获取当前登录用户id,设置购物车对象  15:20 完成任务
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //2:创建查询对象
        LambdaQueryWrapper<ShoppingCart> queryWrapper =
                new LambdaQueryWrapper<>();
        //3:查询当前登录用户购物车
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        //4:如果有dishId 查询菜口
        queryWrapper.eq(shoppingCart.getDishId() != null,
                ShoppingCart::getDishId, shoppingCart.getDishId());
        //5:如果有setmealId 查询套餐
        queryWrapper.eq(shoppingCart.getSetmealId() != null,
                ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        //6:查询购物车满足条件对象
        ShoppingCart shopCart = shoppingCartService.getOne(queryWrapper);
        //7:如果查询结果(买过)
        //7.1:数量为1保存购物车

        Integer number = shopCart.getNumber();
        if (number <= 1) {
            return R.error("最少数量为1");
        }
        shopCart.setNumber(number - 1);
        shoppingCartService.updateById(shopCart);
        return R.success(shopCart);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper =
                new LambdaQueryWrapper<>();
        //3:查询当前登录用户购物车
        queryWrapper.eq(ShoppingCart::getUserId,
                BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

}