package com.itheima.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.entity.AddressBook;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    //任务一 新增地址簿
    //http://localhost:8080/addressBook
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址簿 {}", addressBook);
        //少一步，知道这个地址是谁的
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }


    /**
     * 查询当前登录用户地址列表。 09:50 完成任务
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        //1:获取当前登录用户id
        addressBook.setUserId(BaseContext.getCurrentId());
        //2:创建查询条件
        LambdaQueryWrapper<AddressBook> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(),
                AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);

    }

    //设置默认地址
    //put 更新请求
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody
                                         AddressBook addressBook){
        //1.查询当前登录用户默认地址，更新 广州 0
        log.info("设置默认地址 {}",addressBook);
        LambdaUpdateWrapper<AddressBook> queryWrapper =
                new LambdaUpdateWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.set(AddressBook::getIsDefault,0);

        addressBookService.update(queryWrapper);
        //2.为地址default 1
        addressBook.setIsDefault(1);
        //3.更新
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    //第一接口查询:依据id查询地址
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook != null){
            return R.success(addressBook);
        }else{
            return R.error("没有找到该对象");
        }
    }

    //第二接口更新: 更新地址
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        log.info("更新地址 {}",addressBook);
        addressBookService.updateById(addressBook);
        return R.success("更新成功");
    }

    //第三接口删除接口
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        addressBookService.removeById(ids);
        return R.success("delete成功");
    }

    //用户确认订单，查询默认收货地址
    //get   /default
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        //1.创建查询条件，当前用户，默认地址
        LambdaQueryWrapper<AddressBook> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,
                BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        //2.查询一个
        if(one == null){
            return R.error("没有找到指定默认地址，请查询");
        }else {
            return R.success(one);
        }
    }
}
