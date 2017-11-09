package com.lanou.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dllo on 17/11/7.
 *
 * 异常处理类
 */
public class CustomExceptionResolver implements HandlerExceptionResolver{

    //前端控制器DispatcherServlet在进行HandlerMapping,HandlerAdapter执行Handler的过程中,
    //如果遇到异常,就执行这个方法

    //Handler参数是最终要执行的方法(handler),真实身份是HandlerMethod,
    //Exception ex 就是收到的异常信息
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o, Exception ex) {

        //输出异常信息
        ex.printStackTrace();

        CustomException exception = null;
        if (ex instanceof CustomException){
            //shiro的异常
            exception = (CustomException) ex;

        }else {
            //其他异常
            exception = new CustomException("未知异常");
        }

        //获取异常信息
        String msg = exception.getMessage();

        httpServletRequest.setAttribute("msg",msg);

        try {
            httpServletRequest.getRequestDispatcher("/WEB-INF/error.jsp").forward(httpServletRequest,httpServletResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView();
    }
}
