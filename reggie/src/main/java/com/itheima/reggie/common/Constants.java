package com.itheima.reggie.common;

//常量类
//记录项目通用常量
public final class Constants {

    //雇员状态 已禁用
    public final static int EMPLOYEE_DISABLED=0;
    //雇员状态 正常用
    public final static int EMPLOYEE_ENABLED=1;

    //雇员登录成功后session值
    public final static String EMPLOYEE_LOGIN_KEY = "employee";

    //创建雇员初始密码
    public final static String EMPLOYEE_INIT_PASSWORD = "123456";

    //未登录
    public static final String NOTLOGIN = "NOTLOGIN";
    public static final String CREATE_EMPLOYEE_SUCCESS = "新增雇员成功";

    public static final String OK_USERNAME = "该用户未被占用，欢迎使用";
    public static final String ERROR_USERNAME = "该用户名己被使用";
    public static final String MESSAGE_ADDEMP_OK = "该用户已更新";
    public static final String MESSAGE_EMP_NOTFOUND = "没有查询到雇员信息";
    public static final String MESSAGE_ADD_CATEGORY_OK = "添加分类成功";
    //套餐在售与停售
    public static final Integer SETMEALL_ONLINE = 1;
    public static final Integer SETMEALL_UNONLINE = 0;
    //用户登录key
    public static final String USER_LOGIN_KEY = "user";
    //用户短信验证码key
    public static final String USER_LOGIN_CODE = "sms";
}

