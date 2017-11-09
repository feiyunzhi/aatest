package com.lanou.base;

//import com.lanou.utils.VerifyCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dllo on 17/11/8.
 */
@Controller
public class BaseController {

    //到登录界面
    @RequestMapping(value = "/home")
    public String home() {
        return "login";
    }

    //到管理界面
    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }


}
