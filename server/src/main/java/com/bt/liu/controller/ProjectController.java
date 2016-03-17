package com.bt.liu.controller;

import com.bt.liu.entity.ProjectVo;
import com.bt.liu.service.ProjectService;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.Constants;
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
 * Created by binglove on 16/3/12.
 *
 * 项目业务层
 */
@Controller
public class ProjectController extends BaseController {

    @Autowired
    private ProjectService projectService;


    @RequestMapping("/project/index")
    public void queryProjects(ModelMap modelMap) {
        List<ProjectVo> projects = projectService.queryProjects();
        modelMap.addAttribute("projects", projects);
    }

    @Permission
    @RequestMapping("/project/save/{type}")
    public String saveProject(ProjectVo projectVo,HttpSession session,@PathVariable("type") int type) {
        if(type != Constants.ADD && type != Constants.EDIT) {
            throw new RuntimeException("非法的请求操作!");
        }
        if(StringUtils.isBlank(projectVo.getCode()) || !projectVo.getCode().matches("^\\w+$")) {
            session.setAttribute("message","项目编码由数字字母下划线组成,不能为空!");
        } else if(StringUtils.isBlank(projectVo.getName())) {
            session.setAttribute("message","项目名称不能为空!");
        } else {
            BaseRes baseRes = projectService.saveProject(projectVo, type);
            if(baseRes.isSucccess()) {
                return "redirect:/project/index";
            }
            session.setAttribute("message",baseRes.getError());
        }
        return "redirect:/project/new";
    }

    @Permission
    @RequestMapping("/project/new")
    public void newProject(ProjectVo projectVo,ModelMap modelMap) {
        if(StringUtils.isNotBlank(projectVo.getCode())) {
            projectVo = projectService.getProject(projectVo.getCode());
        }
        modelMap.addAttribute("project", projectVo);
    }

    @Permission
    @RequestMapping("/project/delete")
    public String newProject(ProjectVo projectVo) {
        if(StringUtils.isNotBlank(projectVo.getCode())) {
            projectService.delProject(projectVo);
        }
        return "redirect:/project/index";
    }




}
