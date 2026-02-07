package com.itheima;

import com.itheima.utils.ValidateCodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCode {

    @Test
    public void test01(){

        Integer i = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(i);
        i = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(i);
        String s = ValidateCodeUtils.generateValidateCode4String(6);
        System.out.println(s);
    }
}