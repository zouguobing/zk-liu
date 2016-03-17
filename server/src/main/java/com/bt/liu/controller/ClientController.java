package com.bt.liu.controller;

import com.bt.liu.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by binglove on 16/3/12.
 *
 * 客户端业务层
 */
@Controller
public class ClientController extends BaseController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/queryClients")
    public void queryClients(ModelMap modelMap) {
        modelMap.addAttribute("clients", clientService.queryClients());
    }

}
