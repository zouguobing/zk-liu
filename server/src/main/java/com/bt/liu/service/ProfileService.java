package com.bt.liu.service;

import com.bt.liu.support.ZkSerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by binglove on 16/3/14.
 */
@Service
public class ProfileService {

    @Autowired
    private ZkSerClient zkSerClient;

    public List<Map<String,String>> prefetchByModulePath(String modulePath) {
        List<Map<String,String>> resList = new ArrayList<>();
        if(zkSerClient.existNode(modulePath)) {
            List<String> children = zkSerClient.getChildren(modulePath);
            if(children != null && !children.isEmpty()) {
                resList = children.parallelStream().map(this::decorateKey).collect(Collectors.toList());
            }
        }
        return resList;
    }

    private Map<String,String> decorateKey(String key) {
        Map<String,String> map = new HashMap<>(1);
        map.put("key",key);
        return map;
    }
}
