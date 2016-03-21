package com.bt.liu.dto;

import com.bt.liu.NetUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by binglove on 16/3/17.
 */
public class LiuClient {

    //连接的项目编号
    private String projectCode;
    //连接的项目编号下的模块, * 代表 all
    private String modules;
    //环境development  test  production
    private String profile;
    //客户端ip
    private String ip;
    //应用名称
    private String applicationName;
    //连接时间
    private String connectedTime;

    public LiuClient() {
    }

    public LiuClient(String applicationName, String profile, String projectCode, String modules) {
        this.applicationName = applicationName;
        this.connectedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.ip = NetUtils.getLocalHost();
        this.modules = modules;
        this.profile = profile;
        this.projectCode = projectCode;
    }

    public String getConnectedTime() {
        return connectedTime;
    }

    public void setConnectedTime(String connectedTime) {
        this.connectedTime = connectedTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
