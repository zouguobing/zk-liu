package com.bt.liu.support;

import com.bt.liu.entity.User;
import com.github.zkclient.ZkClient;

import java.util.List;

/**
 * Created by binglove on 16/3/12.
 */
public class ZkSerClient extends ZkClient {

    //根路径
    public static final String ROOT_PATH = "/liu/";



    public ZkSerClient(String connectString) {
        super(connectString);
        init();
    }

    private void init() {
        Constants.ENV_LIST.stream().filter(env -> !existNode(env)).forEach(env -> createPersistent(ROOT_PATH + env,true));
        if(!existNode(Constants.CLIENT_ZK_DIR)) {
            createPersistent(Constants.CLIENT_ZK_DIR);
        }
        if(!existNode(Constants.USER_ZK_DIR)) {
            createPersistent(Constants.USER_ZK_DIR);
        }
        if(!existNode(Constants.USER_ZK_DIR + Constants.separator + "admin")) {
            User user = new User();
            user.setPassword(MD5.getInstance().md5("admin"));
            user.setNickName("超级管理员");
            user.setPermission("*");
            user.setUserName("admin");
            createPersistent(Constants.USER_ZK_DIR + Constants.separator + "admin",user);
        }
    }

    //节点是否存在
    public boolean existNode(String path) {
        return exists(ROOT_PATH + path);
    }

    //创建节点
    public void createPersistent(String path, Object obj) {
        byte[] objBytes = KryoSerialization.serialize(obj);
        createPersistent(ROOT_PATH + path, objBytes);
    }

    //读取节点数据
    public <T> T readData(String path,Class<T> clazz) {
        return KryoSerialization.deserialize(readData(ROOT_PATH + path),clazz);
    }

    //写入节点数据
    public void writeData(String path,Object obj) {
        byte[] objBytes = KryoSerialization.serialize(obj);
        writeData(ROOT_PATH + path,objBytes);
    }



    public void createPersistent(String path) {
        super.createPersistent(ROOT_PATH + path);
    }

    @Override
    public boolean deleteRecursive(String path) {
        return super.deleteRecursive(ROOT_PATH + path);
    }

    @Override
    public List<String> getChildren(String path) {
        return super.getChildren(ROOT_PATH + path);
    }


    public int countChildren(String path) {
        return super.countChildren(ROOT_PATH + path);
    }


    @Override
    public boolean delete(String path) {
        return super.delete(ROOT_PATH + path);
    }
}
