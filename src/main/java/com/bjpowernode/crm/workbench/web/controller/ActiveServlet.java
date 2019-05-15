package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.PageDataVO;
import com.bjpowernode.crm.workbench.serivce.ActiveService;
import com.bjpowernode.crm.workbench.serivce.impl.ActiveServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  19:34
 */
public class ActiveServlet extends MyServlet {
    ActiveService acticeService = null;
    UserService userService = null;
    public void insertActive(HttpServletRequest request, HttpServletResponse response){
        acticeService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());
        String id = UUIDUtil.getUUID();
        Activity activity = (Activity) RequestUtil.init(request, Activity.class);
        activity.setId(id);
        String date = DateTimeUtil.getSysTime();
        activity.setCreateTime(date);
        activity.setCreateBy(((User) request.getSession().getAttribute("userInfo")).getId());
        boolean flag = acticeService.insertActive(activity);
        if(flag){
            PrintJson.printJsonFlag(response, true);
        }else{
            PrintJson.printJsonFlag(response, false);
        }
    }

    public void selectUserList(HttpServletRequest request, HttpServletResponse response){
        userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        //UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list = userService.getUserList();
        if(list!=null&&list.size()>0){
            PrintJson.printJsonObj(response, list);
        }
    }

    public void pageActivityShow(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进入pageActivityShow方法");
        acticeService = (ActiveService)ServiceFactory.getService(new ActiveServiceImpl());
        Map<String,Object> map = new HashMap<>();
        String pageCurrent = request.getParameter("pageCurrent");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if(pageCurrent==null){
            pageCurrent = "1";
        }
        if(pageSize == null){
            pageSize = "5";
        }
        map.put("start",(Integer.parseInt(pageCurrent)-1)*Integer.parseInt(pageSize));
        map.put("count",Integer.parseInt(pageSize));
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        PageDataVO<Activity> pageDataVO = acticeService.selectActivityPageData(map);
        PrintJson.printJsonObj(response, pageDataVO);
    }

    public void deleteActivityBath(HttpServletRequest request, HttpServletResponse response){
        acticeService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());
        String[] params = request.getParameterValues("id");

        boolean flag = acticeService.deleteActivityBath(params);

        PrintJson.printJsonFlag(response, flag);
    }

    public void selectUserListAndActivity(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        acticeService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());
        HashMap<String, Object> hashMap = acticeService.selectActivityById(id);
        PrintJson.printJsonObj(response, hashMap);
    }

    public void updateActivity(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        acticeService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());
        Activity activity = (Activity) RequestUtil.init(request, Activity.class);
        User user = (User) request.getSession().getAttribute("userInfo");
        activity.setEditBy(user.getId());
        activity.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = acticeService.updateActivity(activity);
        PrintJson.printJsonFlag(response, flag);
    }
}
