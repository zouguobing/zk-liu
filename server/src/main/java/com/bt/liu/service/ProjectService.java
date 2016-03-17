package com.bt.liu.service;

import com.bt.liu.entity.ProjectVo;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.Constants;
import com.bt.liu.support.ZkSerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binglove on 16/3/10.
 */
@Service
public class ProjectService {

    @Autowired
    private ZkSerClient zkSerClient;


    //load所有的project
    public List<ProjectVo> queryProjects() {
        List<String> children = zkSerClient.getChildren(Constants.ENV_DEV);
        List<ProjectVo> projects = null;
        if (children != null && !children.isEmpty()) {
            projects = children.stream().map(childPath -> zkSerClient.readData(Constants.ENV_DEV + Constants.separator + childPath, ProjectVo.class)).collect(Collectors.toList());
        }
        return projects;
    }


    public BaseRes saveProject(ProjectVo projectVo, int type) {
        String projectPath = projectVo.getCode();
        BaseRes baseRes = new BaseRes();
        if (type == Constants.ADD) {
            if (zkSerClient.existNode(Constants.ENV_DEV + Constants.separator + projectPath)) {
                return baseRes.setError("项目" + projectPath + "已存在!");
            }
            //创建project
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.createPersistent(env + Constants.separator + projectPath, projectVo));
        } else if (type == Constants.EDIT) {
            if (!zkSerClient.existNode(Constants.ENV_DEV + Constants.separator + projectPath)) {
                return baseRes.setError("项目" + projectPath + "不存在!");
            }
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.writeData(env + Constants.separator + projectPath, projectVo));
        }
        return baseRes;
    }


    public void delProject(ProjectVo projectVo) {
        String projectPath = projectVo.getCode();
        if (zkSerClient.existNode(Constants.ENV_DEV + Constants.separator + projectPath)) {
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.deleteRecursive(env + Constants.separator + projectPath));
        }
    }


    public ProjectVo getProject(String projectCode) {
        if (zkSerClient.existNode(Constants.ENV_DEV + Constants.separator + projectCode)) {
            return zkSerClient.readData(Constants.ENV_DEV + Constants.separator + projectCode, ProjectVo.class);
        }
        return new ProjectVo();
    }



    public boolean existProject(String projectPath) {
        return zkSerClient.existNode(projectPath);
    }



    public String getFirstModule(String projectPath) {
        List<String> children = zkSerClient.getChildren(projectPath);
        return (children != null && !children.isEmpty()) ? children.get(0) : null;
    }


    public List<String> getAllModule(String projectPath) {
        List<String> children = zkSerClient.getChildren(projectPath);
        return children;
    }


}
