package com.itheima.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.entity.User;
import com.itheima.reggie.common.Constants;
import com.itheima.reggie.common.R;
import com.itheima.service.UserService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 作用:
     * 1:接收用户添加手机号
     * 2:生成验证验证码并且发送(阿里云)到用户手机
     * @param user  phone
     * @param request
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user,
                             HttpServletRequest request){
        //1:获取手机号
        String phone = user.getPhone();
        //2:判断用户手机号是否为空
        if(StringUtils.isNotEmpty(phone)) {
            //2.1:生成4位数字短信验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            log.info("验证码 {}",code);
            //2.2:通过阿里云发送
            //SMSUtils.sendMessage(phone,code);
            //2.3:保存短信验证把 session 为登录验证
            request.getSession().setAttribute(
                    Constants.USER_LOGIN_CODE,code.toString());
            //2.4:发送成功
            return R.success("手机验证码发送成功");
        }
        //2.5:短信发送失败
        return R.success("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     * @param  map 获取手机号 短信验证码(没有实体类对应)
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,
                         HttpServletRequest request){
        //1:获取手机号与验证码(用户输入)。 14:50 休息一下
        String phone  = (String)map.get("phone");
        String code = (String)map.get("code");
        //2:从session中获了验证码(上一个功能sendmsg)
        String code2 = (String)request.getSession()
                .getAttribute(Constants.USER_LOGIN_CODE);
        //3:如果验证码不为空并且两个相等（登录成功，注册成功）
        if(code2 != null && code2.equals(code)) {
            //  任务：己有用户或者新用户
            //4:创建查询条件
            LambdaQueryWrapper<User> queryWrapper =
                    new LambdaQueryWrapper<>();
            //5:依据用户phone查询用户对象
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //6:如果查询结果空(新用户)
            if(user == null) {
                //7:创建User 对象保存
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //8:用户id保存session
            request.getSession().setAttribute(
                    Constants.USER_LOGIN_KEY,user.getId()
            );
            //9:返回成功结果
            return R.success(user);
        }
        //10:如果验证码为空或者不相同返回登录失败
        return  R.error("登录失败");
    }

}