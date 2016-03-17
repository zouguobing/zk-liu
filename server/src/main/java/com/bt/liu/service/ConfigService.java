package com.bt.liu.service;

import com.bt.liu.entity.ConfigVo;
import com.bt.liu.entity.Page;
import com.bt.liu.support.BaseRes;
import com.bt.liu.support.Constants;
import com.bt.liu.support.ZkSerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by binglove on 16/3/12.
 */
@Service
public class ConfigService {


    @Autowired
    private ZkSerClient zkSerClient;

    public BaseRes save(ConfigVo configVo,int type,String profile
            , String projectCode, String moduleName) {
        BaseRes baseRes = new BaseRes();
        String configPath = new StringBuilder(profile).append(Constants.separator).append(projectCode).append(Constants.separator).append(moduleName).append(Constants.separator).append(configVo.getKey()).toString();
        if (type == Constants.ADD) {
            if(zkSerClient.existNode(configPath)) {
                return baseRes.setError("配置key:"+configVo.getKey()+"已存在!");
            }
            String addPath = configPath.substring(configPath.indexOf(Constants.separator));
            Constants.ENV_LIST.stream().forEach(env -> zkSerClient.createPersistent(env+addPath,configVo));
        } else if(type == Constants.EDIT) {
            if(!zkSerClient.existNode(configPath)) {
                return baseRes.setError("配置key:"+configVo.getKey()+"不存在!");
            }
            zkSerClient.writeData(configPath,configVo);
        }
        return baseRes;
    }

    /**
     * 分页数据
     * @param modulePath
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<ConfigVo> getConfigListByModuleAndPageInfo(String modulePath, int pageNum, int pageSize) {
        int totalCount = 0;
        if(!zkSerClient.existNode(modulePath) || (totalCount = zkSerClient.countChildren(modulePath)) == 0) return new Page<>(1,pageSize,totalCount);
        List<String> children = zkSerClient.getChildren(modulePath);
        List<ConfigVo> configVoList = children.stream().skip((pageNum - 1) * pageSize).limit(pageSize).map(e -> modulePath + Constants.separator + e).map(c -> zkSerClient.readData(c,ConfigVo.class)).collect(Collectors.toList());
        return new Page<>(pageNum,pageSize,totalCount,configVoList);
    }



    public ConfigVo getConfigByPath(String configPath) {
        if(!zkSerClient.existNode(configPath)) return null;
        return zkSerClient.readData(configPath,ConfigVo.class);
    }

}
