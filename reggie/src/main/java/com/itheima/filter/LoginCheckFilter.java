package com.itheima.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.Constants;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(filterName = "loginCheckFilter",urlPatterns ="/*" )
//创建过滤器
//filterName = "loginCheckFilter" 过滤器名称
//urlPatterns ="/*" 过滤器地址
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns ="/*" )
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    //过滤器初始化函数：第一次初始变量
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器启动成功.....");
        Filter.super.init(filterConfig);
    }

    //核心功能：每一次过滤器都会执行doFilter 过滤
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Long id = Thread.currentThread().getId();
        log.info("1.filter --- 当前线程id {}",id);
        //1.对象转换 ServletRequest -> HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //2.获取本次请求url地址(去哪)
        String requestURI = request.getRequestURI();
        log.info("拦截请求:{}", requestURI);
        //3.创建数组（旅行地址）/employee/login/backend/**
        //登录 退出 注册 网页放行
        String[] urls = new String[]{
                "/employee/login",    //员工 登录
                "/employee/logout",   //员工 退出
                "/backend/**",
                "/front/**",
                "/user/sendMsg",      //放行 用户发短信验证码
                "/user/login"         //     用户登录
        };
        //4.检查本次请求是否符合标准（单独写一个函数） true 放行 false 拦截（再次判断）
        boolean check = check(urls, requestURI);
        //5.满足条件放行
        if(check){
            log.info("本次请求{}不需要处理-放行", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //6.下面操作必须先登录成功后才能通过
        //7.判断员工session对象是否保存员工id(员工id 凭证)
        if(request.getSession()
                .getAttribute(Constants.EMPLOYEE_LOGIN_KEY) != null){
            log.info("用户己登录,用户id为{}",
                    request.getSession().getAttribute(
                            Constants.EMPLOYEE_LOGIN_KEY));
            Long empId =(Long) request.getSession().getAttribute(
                    Constants.EMPLOYEE_LOGIN_KEY
            );
            BaseContext.setCurrentId(empId);
            //8:如果存在放行
            filterChain.doFilter(request,response);
            return;
        }
        //8.判断员工session对象是否保用户工id(用户id 凭证)
        //8.1判断session是否有用户id，不为空(如果session存在可以访问私有资源)
        if(request.getSession().getAttribute(Constants.USER_LOGIN_KEY)
                 !=null) {
            //8.2获取用户id
            Long userId =(Long)request.getSession()
                    .getAttribute(Constants.USER_LOGIN_KEY);
            //8.3保存BaseContext中
            BaseContext.setCurrentId(userId);
            //8.4放行 返回
            filterChain.doFilter(request, response);
            return;
        }


        //9.权限不存在返回未登录
         response.getWriter().write(JSON.toJSONString(R.error(
                 Constants.NOTLOGIN
         )));
        return;
        //20.所有请求放行（**）进入下一个过滤器
        //filterChain.doFilter(request, response);
    }

    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;//满足某个条件
            } else {
            }
        }return false;//一个都没匹配成功


    }
    //销毁（如果过滤不再使用，销毁资料（数据库/文件））
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
