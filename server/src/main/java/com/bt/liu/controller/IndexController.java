package com.bt.liu.controller;

import com.bt.liu.entity.ProjectVo;
import com.bt.liu.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by binglove on 16/3/10.
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private ProjectService projectService;


    @RequestMapping("/error")
    public String error() {
        return "error";
    }

    @RequestMapping("/")
    public String rootIndex() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        List<ProjectVo> projects = projectService.queryProjects();
        modelMap.addAttribute("projects", projects);
        return "index";
    }

}
