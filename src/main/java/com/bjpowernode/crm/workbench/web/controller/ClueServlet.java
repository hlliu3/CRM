package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MyServlet;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.serivce.ClueService;
import com.bjpowernode.crm.workbench.serivce.impl.ClueServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  22:40
 */
public class ClueServlet extends MyServlet {
    public void selectUserList(HttpServletRequest request,HttpServletResponse response){
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> userList = clueService.selectUserList();
        PrintJson.printJsonObj(response, userList);
    }
}
