package com.itheima.controller;

//商家雇员控制器

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.entity.Employee;
import com.itheima.reggie.common.Constants;
import com.itheima.reggie.common.R;
import com.itheima.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //http://127.0.0.1:8080/employee/login
    @PostMapping("/login")
    //写HttpServletRequest request,参数是为了创建session
    // @RequestBody Employee employee客户传来是json对象
    public R<Employee> login(HttpServletRequest request,
                             @RequestBody Employee employee) {
        //1:将页面提交的密码password进行md5加密处理
        //123456 明文  -->加密--> e10adc3949ba59abbe56e057f20f883e
        //md5()加密
        //如何增强密码安全fntg
        // (1) 只要用户密码够强壮
        // (2) 多次循环加密
        // (3) 分段加密     （12）+（3456）
        //12345   abcde 弱密码
        //8位以上，有数字，小写字母 大写字母 特殊符号  强壮
        //190a#)A__8do
        //存在数据库。 e10adc3949ba59abbe56e057f20f883e
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //System.out.println(password);
        //2:根据页面提交用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3:如果没有查询则返回登录失败
        if (emp == null) {
            return R.error("登录失败");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //4:查看用户状态如果己禁用，返回员工己禁用结果
        if (emp.getStatus() == Constants.EMPLOYEE_DISABLED) {
            return R.error("登录失败");
        }
        //5:登录成功，将用员工id存入session对象中  创建sessiom对象 生成session id 将生成好的id发送给用户
        request.getSession().setAttribute(Constants.EMPLOYEE_LOGIN_KEY,
                emp.getId());
//        request.getSession().setAttribute("role",
//                emp.getType());
        //session 钥匙
        System.out.println(request.getSession().getId());
        //6:返回成功消息使用通用结果类
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //1.清除session保存数据
        request.getSession()
                .removeAttribute(Constants.EMPLOYEE_LOGIN_KEY);
        //2.返回退出
        return R.success("退出成功");
    }
    /*
     * 请求地址 http://127.0.0.1:8080/employee
     * 请求方式 post
     * 请求参数 json{username,password,age...}
     *         request 使用session对象
     *         @RequestBody employee 参数json
     * 响应结果  R
     * @param request
     * @param employee
     * @return
     */
     @PostMapping
    public R<String> save(HttpServletRequest request,
                          @RequestBody Employee employee) {
        log.info("新增雇员信息: {}", employee.toString());
        Long id = Thread.currentThread().getId();
        log.info("2.---当前线程id: {}", id);
        //1.设置初始密码123456
        String password = DigestUtils.md5DigestAsHex(
                Constants.EMPLOYEE_INIT_PASSWORD.getBytes()
        );
        employee.setPassword(password);
        //2.设置当前用户创建时间
        //employee.setCreateTime(LocalDateTime.now());
        //3.设置用户修改时间
        //employee.setUpdateTime(LocalDateTime.now());
        //4.获取当前登录用户id
        //Long loginEmpId = (long)request.getSession().getAttribute(
              //  Constants.EMPLOYEE_LOGIN_KEY
       // );
        //5.设置雇员创建用户id(登录用户id)
        //employee.setCreateUser(loginEmpId);
        //6.设置雇员更新用户id(登录用户id)
        //employee.setUpdateUser(loginEmpId);
        //7.保存新用户（用户不能重复）
        employeeService.save(employee);
        //8.返回保存成功消息
        return R.success(Constants.CREATE_EMPLOYEE_SUCCESS);
    }


    @GetMapping("/exist")
    public R<String> existUserName(String username){
        //1:查询当前用户名是否存在
        LambdaQueryWrapper<Employee> queryWrapper =
                new LambdaQueryWrapper<Employee>();
        queryWrapper.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp == null){
            return R.success(Constants.OK_USERNAME);
        }else{
            return R.error(Constants.ERROR_USERNAME);
        }

        //2:如果存在  当前用户名己存在  error
        //3:不存在。  欢迎使用         success
    }

    @GetMapping("/page")
    public R<Page> page(
            @RequestParam(required = false,defaultValue = "1")
                    int page,
            @RequestParam(required = false,defaultValue = "10")
                    int pageSize,
            String name){   //name "李" null
        //1:创建分页对象
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //2:创建条件查询对象
        LambdaQueryWrapper<Employee> queryWrapper
                = new LambdaQueryWrapper<Employee>();
        //3:如果name不为空,添加like条件
        queryWrapper.like(StringUtils.isNotEmpty(name),
                Employee::getName,name)
        ;             //4:排序降序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //5:分页查询
        employeeService.page(pageInfo,queryWrapper);
        //6:返回结晃
        return R.success(pageInfo);
    }
    /*
    * request 使用session对象
    * employee [id:1,status:1]
    * */
    @PutMapping
    public R<String> update(HttpServletRequest request,
                            @RequestBody Employee employee) {
        log.info("更新用户状态{}", employee.toString());
        //1.获取当前登录用户 id admin
        Long empId = (Long) request.getSession().getAttribute(
                Constants.EMPLOYEE_LOGIN_KEY
        );
        //2.修改雇员对象更新时间
        employee.setUpdateTime(LocalDateTime.now());
        //3.修改雇员更新用户
        employee.setUpdateUser(empId);
        //4.调用service更新
        employeeService.updateById(employee);
        //5.返回结果 更新成功
        return R.success(Constants.MESSAGE_ADDEMP_OK);
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee emp = employeeService.getById(id);
        if(emp != null){
            return R.success(emp);
        }
        return R.error(Constants.MESSAGE_EMP_NOTFOUND);
    }


}
