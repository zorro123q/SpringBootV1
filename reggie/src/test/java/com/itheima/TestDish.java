package com.itheima;

import com.itheima.dto.DishDto;
import com.itheima.service.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDish {

    @Autowired
    private DishService dishService;

    @Test
    public void test01(){
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(1966813838260039681l);
        System.out.println(byIdWithFlavor);
    }
}