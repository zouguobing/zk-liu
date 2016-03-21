package com.bt.liu.listener;

import com.bt.liu.KryoSerialization;
import com.bt.liu.ZkLiuClient;
import com.bt.liu.dto.LiuConfig;
import com.github.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by binglove on 16/2/19.
 */
public class ZkLiuDataListener implements IZkDataListener {


    static Logger logger = LoggerFactory.getLogger(ZkLiuDataListener.class);

    private ZkLiuClient zkLiuClient;

    public ZkLiuDataListener(ZkLiuClient zkLiuClient) {
        this.zkLiuClient = zkLiuClient;
    }

    @Override
    public void handleDataChange(String dataPath, byte[] data) throws Exception {
        LiuConfig liuConfig = KryoSerialization.deserialize(data, LiuConfig.class);
        if (logger.isInfoEnabled()) {
            logger.info("更新配置节点数据 profile/projectCode/module/node -> {} value -> ", dataPath.replace(ZkLiuClient.ROOT_PATH, ""), liuConfig.getValue());
        }
        int index = dataPath.lastIndexOf("/");
        String modulePath = dataPath.substring(0, index);
        String nodeName = dataPath.substring(index + 1);
        String moduleName = modulePath.substring(modulePath.lastIndexOf("/") + 1);
        zkLiuClient.putValue(moduleName + "." + nodeName, liuConfig.getValue());
    }


    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        //节点数据删除暂不考虑...
        if (logger.isInfoEnabled()) {
            logger.info("删除配置节点数据 profile/projectCode/module/node -> {} value -> ", dataPath.replace(ZkLiuClient.ROOT_PATH, ""));
        }
    }
}
