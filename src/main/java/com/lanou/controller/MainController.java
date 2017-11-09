package com.lanou.controller;

import com.lanou.exception.CustomException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dllo on 17/11/7.
 */
@Controller
public class MainController {


    //用户登录页面
    @RequestMapping(value = "/login")
    public String login(){

        //在登录后再次访问登录界面,直接到登出界面
        if (SecurityUtils.getSubject().isAuthenticated()){
            return "redirect:/home";
        }

        //在登录后再次进行登录的时候将用户登出
//        if (SecurityUtils.getSubject().isAuthenticated()){
//            SecurityUtils.getSubject().logout();
//        }

        return "login";
    }

    //用户登录表单验证
    @RequestMapping(value = "/loginsubmit")
    public String loginsubmit(HttpServletRequest request) throws Exception {
        //在shiro认证过程中出现错误后,将异常类路径通过request返回

        String exceptionClassName =
                (String) request.getAttribute("shiroLoginFailure");

        if (exceptionClassName.equals(UnknownAccountException.class.getName())){
            throw new CustomException("账户名不存在");
        }else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)){
            throw new CustomException("密码不正确");
        }else {
            throw new Exception();
        }

    }
}
