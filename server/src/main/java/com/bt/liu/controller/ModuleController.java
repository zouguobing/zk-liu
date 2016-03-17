package com.bt.liu.controller;

import com.bt.liu.service.ModuleService;
import com.bt.liu.support.Constants;
import com.bt.liu.support.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by binglove on 16/3/12.
 */
@Controller
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/module/save")
    public String save(String profile, String projectCode, String moduleName) {
        moduleService.save(new StringBuilder(profile).append(Constants.separator).append(projectCode).append(Constants.separator).append(moduleName).toString());
        return "redirect:/profile/" + profile + "/" + projectCode;
    }

    @Permission
    @RequestMapping("/module/delete/{profile}/{projectCode}/{moduleName}")
    public String delete(@PathVariable String profile, @PathVariable String projectCode,
                         @PathVariable String moduleName) {
        String modulePath = new StringBuilder(profile).append(Constants.separator).append(projectCode).append(Constants.separator).append(moduleName).toString();
        moduleService.delete(modulePath);
        return "redirect:/profile/" + profile + "/" + projectCode;
    }

}
