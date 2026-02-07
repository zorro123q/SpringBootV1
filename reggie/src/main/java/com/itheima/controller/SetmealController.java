package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.dto.SetmealDto;
import com.itheima.entity.Category;
import com.itheima.entity.Dish;
import com.itheima.entity.Setmeal;
import com.itheima.mapper.SetmealMapper;
import com.itheima.reggie.common.CustomeException;
import com.itheima.reggie.common.R;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealMapper setmealMapper;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套信息 {}",setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page>page(
            @RequestParam(required = false, defaultValue = "1")
                    int page,
            @RequestParam(required = false, defaultValue = "10")
                    int pageSize,
            String name){
        //1:创建二个分页器对象
        //1.1:Setmal 套餐 SetmealDto
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        //1.2:创建查询条件  Setmal 套餐 name order page
        LambdaQueryWrapper<Setmeal> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),
                Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //1.3:分页查询
        setmealService.page(pageInfo,queryWrapper);
        //1.4:复制 套餐page   套餐dto page（页码；几页） 内容不要
        BeanUtils.copyProperties(pageInfo,dtoPage,
                "records");
        //1.5:获取数据列表
        List<Setmeal> records= pageInfo.getRecords();
        //1.6:创建循环遍历列表
        List<SetmealDto> list = records.stream().map((iteam)->{
            //1.6.1:创建dto对象
            SetmealDto setmealDto = new SetmealDto();
            //1.6.2:对象拷贝 item dto
            BeanUtils.copyProperties(iteam,setmealDto);
            //1.6.3:获取套餐类别id
            Long categoryId = iteam.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //1.6.4:依据id获取类别对象
            //1.6.5:判断如果不为空,保存类别名称
            if(category != null){
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        //1.7:dto page 保存处理过去新列表
        dtoPage.setRecords(list);
        //1.8:返回
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String>  delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }


    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status,
                            @RequestParam List<Long> ids){
        log.info("套餐状态 {}", status);
        log.info("套餐id {}", ids);
        //1.判断参数不能为空
        if(ids == null || ids.isEmpty()){
            throw new CustomeException("套餐id不能为空");
        }
        //2.创建更新对象
        //3.指定对象状态为参数状态
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        //4.创建查询对象
        //5.in id ids
        LambdaQueryWrapper<Setmeal> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        //6.更新 setmapper更新
        setmealMapper.update(setmeal,queryWrapper);
        return R.success("更新成功");
    }

    /*
    * 前端用户首页，套餐分类查询
    * @return
    * */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper
                = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() !=null,
                Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() !=null,
                Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);

    }
}