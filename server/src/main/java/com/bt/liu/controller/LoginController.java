package com.bt.liu.controller;

import com.bt.liu.entity.User;
import com.bt.liu.service.UserService;
import com.bt.liu.support.Anonymous;
import com.bt.liu.support.BaseRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by binglove on 16/3/9.
 */
@Anonymous
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    //登陆页面
    @RequestMapping("/login")
    public String login(User user, ModelMap modelMap, HttpSession session) {
        if(getCurrentUser() != null) return "redirect:/index";
        //登陆验证
        if(StringUtils.isNotBlank(user.getUserName()) && StringUtils.isNotBlank(user.getPassword())) {
            BaseRes<User> baseRes = userService.login(user);
            if(baseRes.isSucccess()) {
                session.setAttribute("user",baseRes.getData());
                return "redirect:/index";
            }
            session.setAttribute("message",baseRes.getError());
        }
        return "login";
    }


    //退出,跳转至登陆页
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        User user = getCurrentUser();
        if(user != null) {
            session.removeAttribute("user");
            setUser(null);
        }
        return "redirect:/login";
    }

}
