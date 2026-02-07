package com.itheima;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//启动类
/*
* @ServletComponentScan
* 注解作用让spring boot 在启动扫描
* @WebServlet @WebFilter @WebListener 注解类
* */
@Slf4j                          // 日志注解
@ServletComponentScan           // 扫描过滤器
@SpringBootApplication          // SpringBoot启动类
@EnableTransactionManagement    //开启事务管理
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动");
    }
}
