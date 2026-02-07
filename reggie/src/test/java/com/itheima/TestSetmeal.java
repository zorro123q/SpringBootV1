package com.itheima;

import com.itheima.service.SetmealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestSetmeal {
    @Autowired
    private SetmealService setmealService;

    @Test
    public void test01(){
        List<Long> ids = new ArrayList<>();
        ids.add(1967125234877157378L);
        setmealService.removeWithDish(ids);
    }
}
