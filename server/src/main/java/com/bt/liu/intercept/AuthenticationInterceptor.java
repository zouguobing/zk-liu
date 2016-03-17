package com.bt.liu.intercept;

import com.bt.liu.controller.BaseController;
import com.bt.liu.entity.User;
import com.bt.liu.support.Anonymous;
import com.bt.liu.support.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by binglove on 16/3/9.
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Object action = handlerMethod.getBean();
            if (!(action instanceof BaseController))
                throw new Exception("request servlet must extends BaseController class info -> " + action.getClass().getName());

            HttpSession session = request.getSession();
            //当前登陆的用户
            User user = (User) session.getAttribute("user");
            //设置用户信息
            ((BaseController) action).setUser(user);
            //如果用户没有登陆,并且访问需要登陆的action
            if(user == null && !isAnonymous(handlerMethod)) {
                response.sendRedirect("/login");
                return false;
            }
            //用户登录状态 访问权限过滤
            if(user != null && !hasPermission(handlerMethod,user)) {
                session.setAttribute("message","木有权限, 如需访问请联系管理员!");
                response.sendRedirect("/error");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("authentication preHandle error -> {}",e);
            throw new RuntimeException("请求出错,请联系技术!");
        }
    }

    //匿名验证
    private boolean isAnonymous(HandlerMethod handlerMethod) throws Exception {
        Object action = handlerMethod.getBean();
        Class actClz = action.getClass();
        if(actClz.getAnnotation(Anonymous.class) != null)
            return true;
        Method method = handlerMethod.getMethod();
        return method.getAnnotation(Anonymous.class) != null;
    }

    //权限验证
    private boolean hasPermission(HandlerMethod handlerMethod,User user) {
        Method method = handlerMethod.getMethod();
        Permission permission = method.getAnnotation(Permission.class);
        if(permission == null) {
            return true;
        }
       return Arrays.asList(permission.userName().split("\\|")).parallelStream().anyMatch(e -> e.equals(user.getUserName()));
    }
}
