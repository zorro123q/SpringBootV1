package com.itheima.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //添加套餐
    public void saveWithDish(SetmealDto setmealDto);
    //删除套餐
    void removeWithDish(List<Long> ids);
}