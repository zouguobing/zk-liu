package com.bt.liu;

import com.bt.liu.listener.ZkLiuChildListener;
import com.bt.liu.listener.ZkLiuDataListener;
import com.github.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by binglove on 16/2/18.
 */
public class ZkLiuClient extends ZkClient {


    static Logger logger = LoggerFactory.getLogger(ZkLiuClient.class);

    //配置管理Map集合, key -> module  value -> module下的node map集合
    private Map<String, Map<String,String>> configMap = new ConcurrentHashMap<>();

    //构建zk
    public ZkLiuClient(String zkServers, String projectCode, String profile, String modules) {
        super(zkServers);
        init(projectCode, profile, modules);
    }

    //初始化...
    private void init(String projectCode, String profile, String modules) {
        logger.info("初始化配置数据[ \"projectCode\":{},\"profile\":{},\"modules\":{}]",projectCode,profile,modules);
        //根路径 + 环境 + 项目编号
        final String parentPath = Constant.ROOT_PATH + profile + "/" + projectCode;

        List<String> moduleChildren = null;
        boolean isModule = StringUtils.isNotBlank(modules);

        if(isModule) {
            moduleChildren = new ArrayList<>();
            moduleChildren.addAll(Arrays.asList(modules.split(",")));
        } else {
            moduleChildren = getChildren(parentPath);
            subscribeChildChanges(parentPath,new ZkLiuChildListener(this,false));
        }

        for (String moduleName : moduleChildren) {
            String modulePath = parentPath + "/" + moduleName;
            Map<String, String> moduleMap = new ConcurrentHashMap<>();
            List<String> nodeChildren = getChildren(modulePath);
            if (nodeChildren != null && !nodeChildren.isEmpty()) {
                for (String nodeName : nodeChildren) {
                    String nodePath = modulePath + "/" + nodeName;
                    moduleMap.put(nodeName, readString(nodePath));
                    subscribeDataChanges(nodePath, new ZkLiuDataListener(this));
                }
            }
            configMap.put(moduleName, moduleMap);
            //订阅module的子节点变更
            subscribeChildChanges(modulePath, new ZkLiuChildListener(this, true));
        }
    }


    public String readString(String path) {
        byte[] res = readData(path,true);
        return res == null ? null : new String(res);
    }



    public Map<String, Map<String,String>> getConfigMap() {
        return configMap;
    }


}
