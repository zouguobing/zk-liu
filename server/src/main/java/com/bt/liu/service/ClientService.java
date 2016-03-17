package com.bt.liu.service;

import com.bt.liu.entity.ClientVo;
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
public class ClientService {


    @Autowired
    private ZkSerClient zkSerClient;

    //所有在线的客户端信息
    public List<ClientVo> queryClients() {
        List<String> children = zkSerClient.getChildren(Constants.CLIENT_ZK_DIR);
        List<ClientVo> clientVoList = null;
        if(children != null && !children.isEmpty()) {
            clientVoList = children.stream().map(childPath -> zkSerClient.readData(Constants.CLIENT_ZK_DIR + Constants.separator + childPath, ClientVo.class)).collect(Collectors.toList());
        }
        return clientVoList;
    }


}
