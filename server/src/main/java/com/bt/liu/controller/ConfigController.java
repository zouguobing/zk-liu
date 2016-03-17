package com.bt.liu.controller;

import com.bt.liu.entity.ConfigVo;
import com.bt.liu.entity.User;
import com.bt.liu.service.ConfigService;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by binglove on 16/3/12.
 * <p/>
 * 配置key value
 */
@Controller
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;



    @RequestMapping("/config/save/{type}")
    public String saveConfig(ConfigVo configVo, String profile
            , String moduleName, String projectCode
            , String queryModule, String queryKey, @PathVariable("type") int type
            , HttpSession session
    ) {
        if(type != Constants.ADD && type != Constants.EDIT) {
            throw new RuntimeException("非法的操作请求!");
        }
        if(StringUtils.isBlank(profile) || !Constants.ENV_LIST.contains(profile) || StringUtils.isBlank(projectCode) || StringUtils.isBlank(moduleName)) {
            throw new RuntimeException("非法的参数请求!");
        }
        if(StringUtils.isBlank(configVo.getKey()) || !configVo.getKey().matches("^\\w+$")) {
            session.setAttribute("message","配置key只能由字母数字下划线组成!");
        } else if(StringUtils.isBlank(configVo.getValue())) {
            session.setAttribute("message","配置value不能为Null!");
        } else {
            User user = getCurrentUser();
            configVo.setOperator(user.getUserName());
            configVo.setUpdateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            BaseRes baseRes = configService.save(configVo,type,profile,projectCode,moduleName);
            if(!baseRes.isSucccess()) {
                session.setAttribute("message",baseRes.getError());
            }
        }
        return "redirect:/profile/"+profile+"/"+projectCode+"/"+queryModule+"?queryKey="+queryKey;
    }

}
