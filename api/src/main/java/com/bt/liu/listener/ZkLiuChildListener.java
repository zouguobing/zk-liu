package com.bt.liu.listener;

import com.bt.liu.Constant;
import com.bt.liu.ZkLiuClient;
import com.github.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by binglove on 16/2/19.
 */
public class ZkLiuChildListener implements IZkChildListener {

    static Logger logger = LoggerFactory.getLogger(ZkLiuChildListener.class);

    private ZkLiuClient zkLiuClient;

    //模块
    private boolean isModule;


    public ZkLiuChildListener(ZkLiuClient zkLiuClient, boolean isModule) {
        this.isModule = isModule;
        this.zkLiuClient = zkLiuClient;
    }

    @Override
    public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
        if(currentChildren == null || currentChildren.isEmpty()) {
            if (logger.isInfoEnabled())
                logger.info("配置项 path -> {}, child is empty...", parentPath.replace(Constant.ROOT_PATH, ""));
            return;
        }
        //场景判断
        if(isModule) {//1. 删除/增加了 配置节点
            String moduleName = parentPath.substring(parentPath.lastIndexOf("/")+1);
            Map<String,String> nodeMap = zkLiuClient.getConfigMap().get(moduleName);
            for (String nodeName : currentChildren) {
                if(!nodeMap.containsKey(nodeName)) {
                    String nodePath = new StringBuilder(parentPath).append("/").append(nodeName).toString();
                    String value = zkLiuClient.readString(nodePath);
                    if (logger.isInfoEnabled())
                        logger.info("更新配置节点数据 profile/projectCode/module/node -> {} value -> ", parentPath.replace(Constant.ROOT_PATH, "") + "/" + nodeName, value);
                    nodeMap.put(nodeName,value == null ? "" : value);
                    zkLiuClient.subscribeDataChanges(nodePath,new ZkLiuDataListener(zkLiuClient));
                }
            }
        } else { //2. 删除/增加了 module节点
            for(String moduleName : currentChildren) {
                if(!zkLiuClient.getConfigMap().containsKey(moduleName)) {
                    if (logger.isInfoEnabled())
                        logger.info("新增配置项 profile/projectCode/module -> {}", parentPath.replace(Constant.ROOT_PATH, "") + "/" + moduleName);
                    zkLiuClient.subscribeChildChanges(parentPath + "/" + moduleName,new ZkLiuChildListener(zkLiuClient,true));
                    zkLiuClient.getConfigMap().put(moduleName,new ConcurrentHashMap<String, String>());
                }
            }
        }

    }

}
