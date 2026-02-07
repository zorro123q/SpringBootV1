package com.itheima.config;

//创建 mp配置类

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    //分页bean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){

        //拦载器对象
        MybatisPlusInterceptor mybatisPlusInterceptor =
                new MybatisPlusInterceptor();
        //1:分页拦载器
        mybatisPlusInterceptor.addInnerInterceptor(
                new PaginationInnerInterceptor()
        );
        return mybatisPlusInterceptor;
    }
}