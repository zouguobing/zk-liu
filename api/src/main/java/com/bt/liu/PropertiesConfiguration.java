package com.bt.liu;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by binglove on 16/1/29.
 */
public class PropertiesConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfiguration.class);


    //zk 服务地址列表
    private String zkServers;

    //项目编号
    private String projectCode;

    //项目下模块
    private String modules;

    //环境
    private String profile;



    //集体配置...
    private Map<String,String> configMap = new ConcurrentHashMap<>();





    public Boolean getBoolean(String key) {
        String value = getProperty(key);
        return null;
    }

    public String  getString(String key) {
        String value = getProperty(key);
        return value;
    }


    public String  getString(String key,String defaultValue) {
        String value = getString(key);
        if(StringUtils.isBlank(value))
            return defaultValue;
        return value;
    }

    
    private String getProperty(String key) {
        return configMap.get(key);

    }
}
