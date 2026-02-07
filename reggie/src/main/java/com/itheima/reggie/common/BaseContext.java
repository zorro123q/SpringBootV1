package com.itheima.reggie.common;

/*
* 基于ThreadLocal封装工具类
* 用于保存当前登录用户的ID
* */
public class BaseContext {
    //1.创建ThreadLocal对象
    private static ThreadLocal<Long> threadLocal
            = new ThreadLocal<Long>();
    //2.创建获取获取雇员id方法
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    //3.创建保存雇员id方法
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
