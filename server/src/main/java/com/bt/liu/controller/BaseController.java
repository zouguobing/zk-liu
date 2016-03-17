package com.bt.liu.controller;

import com.bt.liu.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by binglove on 16/3/9.
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final int pageSize = 20;

    //登陆用户信息
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    protected User getCurrentUser() {
        return userThreadLocal.get();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    @ExceptionHandler
    public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        logger.error(exception.getMessage(), exception);
        try {
            request.getSession().setAttribute("message", exception.getMessage());
            RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/error");
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


}
