package com.bt.liu.service;

import com.bt.liu.entity.User;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.Constants;
import com.bt.liu.support.MD5;
import com.bt.liu.support.ZkSerClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binglove on 16/3/16.
 */
@Service
public class UserService {

    @Autowired
    private ZkSerClient zkSerClient;


    public List<User> queryUsers() {
        return zkSerClient.getChildren(Constants.USER_ZK_DIR).stream().map(e -> Constants.USER_ZK_DIR + Constants.separator + e).map(u -> zkSerClient.readData(u, User.class)).collect(Collectors.toList());
    }


    public BaseRes saveUser(User user) {
        BaseRes base = new BaseRes();
        String userPath = Constants.USER_ZK_DIR + Constants.separator + user.getUserName();
        if(zkSerClient.existNode(userPath)) {
            return base.setError("用户名:"+user.getUserName()+"已存在!");
        }
        if(StringUtils.isBlank(user.getNickName())) user.setNickName(user.getUserName());
        zkSerClient.createPersistent(userPath,user);
        return base;
    }


    public BaseRes<User> login(User user) {
        BaseRes<User> baseRes = new BaseRes<>();
        String userPath = Constants.USER_ZK_DIR + Constants.separator + user.getUserName();
        if(!zkSerClient.existNode(userPath)) {
            return baseRes.setError("用户名不存在!");
        }
        User loginUser = zkSerClient.readData(userPath,User.class);
        if(!loginUser.getPassword().equals(MD5.getInstance().md5(user.getPassword()))) {
            return baseRes.setError("用户名或密码错误!");
        }
        baseRes.setData(loginUser);
        return baseRes;
    }



    public void deleteUser(String userName) {
        String userPath = Constants.USER_ZK_DIR + Constants.separator + userName;
        if(zkSerClient.existNode(userPath)) zkSerClient.delete(userPath);
    }



    public void updatePassword(User user) {
        String userPath = Constants.USER_ZK_DIR + Constants.separator + user.getUserName();
        zkSerClient.writeData(userPath,user);
    }
}
