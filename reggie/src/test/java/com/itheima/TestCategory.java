package com.itheima;

import com.itheima.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCategory {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test01(){
        //categoryService.remove(1965650092754640897l);
        //categoryService.remove(1397844263642378242l);
    }

}