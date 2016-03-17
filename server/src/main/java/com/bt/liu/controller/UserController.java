package com.bt.liu.controller;

import com.bt.liu.entity.User;
import com.bt.liu.service.UserService;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.MD5;
import com.bt.liu.support.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by binglove on 16/3/16.
 */
@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Permission
    @RequestMapping("/user/index")
    public void queryUsers(ModelMap modelMap) {
        List<User> users = userService.queryUsers();
        modelMap.addAttribute("users", users);
    }

    @Permission
    @RequestMapping("/user/new")
    public void newUser() {
    }

    @Permission
    @RequestMapping(value="/user/save")
    public String saveUser(User user,HttpSession session) {
        if(StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 6) {
            session.setAttribute("message","密码不能小于6位字符!");
        } else if(StringUtils.isBlank(user.getUserName()) || !user.getUserName().matches("^\\w+$")) {
            session.setAttribute("message","用户名由字母数字下划线组成!");
        } else {
            user.setPassword(MD5.getInstance().md5(user.getPassword()));
            BaseRes baseRes = userService.saveUser(user);
            if(baseRes.isSucccess()) {
                return "redirect:/user/index";
            }
            session.setAttribute("message",baseRes.getError());
        }
        return "redirect:/user/new";
    }

    @Permission
    @RequestMapping("/user/delete/{userName}")
    public String deleteUser(@PathVariable("userName")String userName) {
        userService.deleteUser(userName);
        return "redirect:/user/index";
    }



    @RequestMapping("/user/password")
    public void password() {

    }



    @RequestMapping("/user/updatePassword")
    public String updatePassword(String password, String newPassword, HttpSession session) {
        User user = getCurrentUser();
        String oldPassword = MD5.getInstance().md5(password);
        if(!oldPassword.equals(user.getPassword()))
            session.setAttribute("message", "原密码检验失败!");
        else {
            user.setPassword(MD5.getInstance().md5(newPassword));
            userService.updatePassword(user);
            session.setAttribute("user",user);
        }
        return "redirect:/user/password";
    }

}
