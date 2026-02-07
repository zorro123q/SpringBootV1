package com.itheima;

import com.itheima.entity.Employee;
import com.itheima.mapper.EmployeeMapper;
import com.itheima.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = ReggieApplication.class)
public class TestEmployee {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void test1(){
        List<Employee> employees =
                employeeMapper.selectList(null);
        System.out.println(employees);
    }
    @Test
    public void test2(){
        List<Employee> list=employeeService.list();
        System.out.println(list);
    }
}
