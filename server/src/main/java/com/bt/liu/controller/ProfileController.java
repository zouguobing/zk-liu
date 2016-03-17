package com.bt.liu.controller;

import com.bt.liu.entity.ConfigVo;
import com.bt.liu.entity.Page;
import com.bt.liu.service.ConfigService;
import com.bt.liu.service.ModuleService;
import com.bt.liu.service.ProfileService;
import com.bt.liu.service.ProjectService;
import com.bt.liu.support.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by binglove on 16/3/13.
 */
@Controller
public class ProfileController extends BaseController {


    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ConfigService configService;


    @RequestMapping("/profile/{profile}/{projectCode}")
    public String profile(@PathVariable("profile") String profile, @PathVariable("projectCode") String projectCode, ModelMap modelMap) {
        if (StringUtils.isBlank(profile) || !Constants.ENV_LIST.contains(profile) || StringUtils.isBlank(projectCode)) {
            throw new RuntimeException("非法的参数请求!");
        }
        String projectPath = new StringBuilder(profile).append(Constants.separator).append(projectCode).toString();
        boolean existPro = projectService.existProject(projectPath);
        if (!existPro) {
            throw new RuntimeException("项目" + projectCode + "不存在!");
        }
        String currModule = projectService.getFirstModule(projectPath);
        if (StringUtils.isBlank(currModule)) {
            modelMap.put("configList", new ArrayList<>());
            modelMap.put("project", projectService.getProject(projectCode));
            modelMap.put("profile", profile);
            modelMap.put("modules", projectService.getAllModule(new StringBuilder(profile).append(Constants.separator).append(projectCode).toString()));
        }
        return currModule != null ? new StringBuilder("redirect:/profile/").append(profile).append("/").append(projectCode).append("/").append(currModule).toString() : "profile/index";
    }

    @RequestMapping("/profile/{profile}/{projectCode}/{moduleName}")
    public String profile(@PathVariable("profile") String profile, @PathVariable("projectCode") String projectCode, @PathVariable("moduleName") String moduleName, Integer pageNum, String queryKey, ModelMap modelMap) {
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (StringUtils.isBlank(profile) || !Constants.ENV_LIST.contains(profile) || StringUtils.isBlank(projectCode) || StringUtils.isBlank(moduleName)) {
            throw new RuntimeException("非法的参数请求!");
        }
        String modulePath = new StringBuilder(profile).append(Constants.separator).append(projectCode).append(Constants.separator).append(moduleName).toString();
        boolean existModule = moduleService.existModule(modulePath);
        if (!existModule) {
            throw new RuntimeException("模块" + moduleName + "不存在!");
        }
        Page<ConfigVo> page = null;
        if (StringUtils.isNotBlank(queryKey)) {
            List<ConfigVo> configVoList = new ArrayList<>(1);
            ConfigVo configVo = configService.getConfigByPath(new StringBuilder(modulePath).append(Constants.separator).append(queryKey).toString());
            if (configVo != null) {
                configVoList.add(configVo);
            }
            page = new Page<>(1, pageSize, configVoList.size(), configVoList);
        } else {
            page = configService.getConfigListByModuleAndPageInfo(modulePath, pageNum, pageSize);
        }
        modelMap.put("page", page);
        modelMap.put("moduleName", moduleName);
        modelMap.put("project", projectService.getProject(projectCode));
        modelMap.put("profile", profile);
        modelMap.put("queryKey", queryKey);
        modelMap.put("modules", projectService.getAllModule(new StringBuilder(profile).append(Constants.separator).append(projectCode).toString()));
        return "profile/index";
    }


    @RequestMapping("/profile/json/{profile}/{projectCode}/{moduleName}")
    @ResponseBody
    public List<Map<String, String>> profileJson(@PathVariable("profile") String profile, @PathVariable("projectCode") String projectCode, @PathVariable("moduleName") String moduleName) {
        return profileService.prefetchByModulePath(new StringBuilder(profile).append(Constants.separator).append(projectCode).append(Constants.separator).append(moduleName).toString());
    }

}
