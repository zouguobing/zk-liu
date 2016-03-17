package com.bt.liu.service;

import com.bt.liu.support.Constants;
import com.bt.liu.support.ZkSerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by binglove on 16/3/12.
 */
@Service
public class ModuleService {

    @Autowired
    private ZkSerClient zkSerClient;

    public void save(String modulePath) {
        if(!zkSerClient.existNode(modulePath)) {
            String tempPath = modulePath.substring(modulePath.indexOf(Constants.separator));
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.createPersistent(env + tempPath));
        }
    }


    public void delete(String modulePath) {
        if(zkSerClient.existNode(modulePath)) {
            String tempPath = modulePath.substring(modulePath.indexOf(Constants.separator));
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.deleteRecursive(env + tempPath));
        }
    }


    public boolean existModule(String modulePath) {
        return zkSerClient.existNode(modulePath);
    }

}
