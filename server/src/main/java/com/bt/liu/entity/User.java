package com.bt.liu.entity;

/**
 * Created by binglove on 16/3/9.
 */
public class User {

    //用户名---仅支持英文数字_组合
    private String userName;
    //昵称
    private String nickName;
    //用户密码
    private String password;
    //用户权限
    private String permission;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
