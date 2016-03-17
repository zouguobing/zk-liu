package com.bt.liu.support;

import java.util.Arrays;
import java.util.List;

/**
 * Created by binglove on 16/3/12.
 */
public class Constants {
    //根路径
    public static final String ROOT_PATH = "/liu/";

    //用户节点父目录
    public static final String USER_ZK_DIR = "users";

    //客户端节点父目录
    public static final String CLIENT_ZK_DIR = "clients";

    //zk路径分隔符
    public static final String separator = "/";

    //新增
    public static final int ADD = 0;

    //编辑
    public static final int EDIT = 1;

    //开发
    public static final String ENV_DEV = "development";
    //测试
    public static final String ENV_TEST = "test";
    //生产
    public static final String ENV_PRO = "production";

    //环境集合
    public static final List<String> ENV_LIST = Arrays.asList(ENV_DEV,ENV_TEST,ENV_PRO);


}
