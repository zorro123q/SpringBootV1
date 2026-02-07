package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.entity.*;
import com.itheima.mapper.OrderMapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CustomeException;
import com.itheima.reggie.common.R;
import com.itheima.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl
        extends ServiceImpl<OrderMapper, Orders>
        implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    //下订单
    @Override
    @Transactional
    public void submit(Orders orders) {
        //1:获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();
        //2:查询当前登录用户购物车列表
        LambdaQueryWrapper<ShoppingCart> wrapper =
                new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCartList =
                shoppingCartService.list(wrapper);
        //3:如果当前登录用户购物车空，抛异常
        if(shoppingCartList == null ||
                shoppingCartList.size() == 0){
            throw new CustomeException("购物车不能为空 ");
        }
        //4:查询当前登录用户信息(phone,age,...)
        User user = userService.getById(userId);
        //5:依据参数中地址id查询收货地址对象
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook =
                addressBookService.getById(addressBookId);
        //6:如果没有地址信息 抛异常
        if(addressBook == null){
            throw new CustomeException("用户地址有误，不能下单");
        }
        //7:生成订单号(1 2 3) mp IdWorker.getId();
        Long orderId = IdWorker.getId();
        //8:创建对象，合计金额（计算不出错误）
        AtomicInteger amount = new AtomicInteger(0);
        //9:创建循环遍历(创建订单详情) <购物车导出--订单详情况>
        List<OrderDetail> orderDetailList =
                shoppingCartList.stream().map((item) -> {
                    //  9.1:订单id 数量 口味 菜单id 套餐id 名称 图片 价格 计算合计
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setNumber(item.getNumber());
                    orderDetail.setDishFlavor(item.getDishFlavor());
                    orderDetail.setDishId(item.getDishId());
                    orderDetail.setSetmealId(item.getSetmealId());
                    orderDetail.setName(item.getName());
                    orderDetail.setImage(item.getImage());
                    orderDetail.setAmount(item.getAmount());
                    //合计?+ - * / 不支持  替换方法 函数 add sub div multiply
                    amount.addAndGet(
                            item.getAmount()
                                    .multiply(new BigDecimal(
                                            item.getNumber())).intValue()
                    );
                    return orderDetail;
                }).collect(Collectors.toList()); //10:50 休息一下

        //10:创建订单
        //  10.1订单id  下订时间 状态 2 总金额 用户id 用户名 联系方式
        //      联系人  电话  地址(省/市/街/详情)
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(1);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(user.getId());
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "" :
                        addressBook.getProvinceName()) +
                        (addressBook.getCityName() == null ? "" :
                                addressBook.getCityName())+
                        (addressBook.getDistrictName() == null ? "":
                                addressBook.getDistrictName())+
                        (addressBook.getDetail() == null ? "" :
                                addressBook.getDetail())
        );
        //11:保存订单    11:20 休息一下
        this.save(orders);
        //12:批量保存订单明细对象
        orderDetailService.saveBatch(orderDetailList);
        //13:清空购物车
        shoppingCartService.remove(wrapper);

    }

}