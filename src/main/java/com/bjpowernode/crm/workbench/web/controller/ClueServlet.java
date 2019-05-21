package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.serivce.ActiveService;
import com.bjpowernode.crm.workbench.serivce.ClueService;
import com.bjpowernode.crm.workbench.serivce.impl.ActiveServiceImpl;
import com.bjpowernode.crm.workbench.serivce.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void insertClue(HttpServletRequest request,HttpServletResponse response){
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = (Clue) RequestUtil.init(request, Clue.class);
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(((User)(request.getSession().getAttribute("userInfo"))).getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        boolean flag = false;
        try {
            flag = clueService.insertClue(clue);
        } catch (Exception e) {
            e.printStackTrace();
            PrintJson.printJsonFlag(response, false);
            return;
        }
        PrintJson.printJsonFlag(response, flag);
    }

    public void pageShowClueList(HttpServletRequest request,HttpServletResponse response){
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        int pageCurrent = Integer.valueOf(request.getParameter("pageCurrent"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        //动态sql查询需要的条件.....
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String source = request.getParameter("source");
        String state = request.getParameter("state");
        String owner = request.getParameter("owner");

        Map<String,Object> map = new HashMap<>();
        map.put("filtrationSize", (pageCurrent-1)*pageSize);
        map.put("pageSize",pageSize);
        //map.put....
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("state",state);
        map.put("owner",owner);

        PageDataVO<Clue> cluePageDataVO = clueService.pageShowClueList(map);

        PrintJson.printJsonObj(response, cluePageDataVO);
    }

    public void selectClueByClueId(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = null;
        try {
            clue = clueService.selectClueByClueId(id);
        } catch (Exception e) {
            e.printStackTrace();

            return;
        }

        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    public void selectActivityByClueId(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("clueId");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activities = null;
        try {
            activities = clueService.selectActivityByClueId(id);
        } catch (Exception e) {
            e.printStackTrace();
            PrintJson.printJsonFlag(response, false);
            return;
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("success", true);
        map.put("activities", activities);
        PrintJson.printJsonObj(response, map);
    }

    public void selectActivityNotByClueId(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("clueId");
        String activityName = request.getParameter("activityName");
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
        map.put("activityName", activityName);
        ActiveService activityService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());

        List<Activity> activities = activityService.selectActivityNotByClueId(map);
        PrintJson.printJsonObj(response, activities);
    }
    
    public void insertActivityForClue(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        ClueActivityRelation clueActivityRelation = null;
        List<ClueActivityRelation> list = new ArrayList<>();
        for (String aid : aids) {
            clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setClueId(id);
            clueActivityRelation.setActivityId(aid);
            list.add(clueActivityRelation);
        }
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        int i = clueService.insertActivityForClue(list);
        PrintJson.printJsonFlag(response,true);
    }

    public void removeActivityForClue(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.removeActivityForClue(id);
        PrintJson.printJsonFlag(response, flag);
    }

    public void selectActivityListByClueId(HttpServletRequest request,HttpServletResponse response){
        String id = request.getParameter("clueId");
        ActiveService activityService = (ActiveService) ServiceFactory.getService(new ActiveServiceImpl());
        List<Activity> activities = null;
        String activityName = request.getParameter("activityName");
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
        map.put("activityName", activityName);
        try {
            activities = activityService.selectActivityListByClueId(map);
        } catch (Exception e) {
            e.printStackTrace();
            PrintJson.printJsonFlag(response, false);
            return;
        }
        HashMap<String,Object> res = new HashMap<>();
        res.put("success", true);
        res.put("activities", activities);
        PrintJson.printJsonObj(response, res);
    }

    public void clueConver(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String flag = request.getParameter("flag");
        String clueId = request.getParameter("clueid");

        Tran tran = null;
        String user = ((User)(request.getSession().getAttribute("userInfo"))).getName();
        if(!"0".equals(flag)){
            tran = new Tran();
            tran.setActivityId(request.getParameter("clueId"));
            tran.setCreateBy(user);
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setExpectedDate(request.getParameter("expectedClosingDate"));
            tran.setName(request.getParameter("tradeName"));
            tran.setMoney(request.getParameter("amountOfMoney"));
            tran.setActivityId(request.getParameter("activityId"));
            tran.setStage(request.getParameter("stage"));

        }

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean tmp = false;
        try {
            tmp = clueService.clueConver(clueId,tran,user);
        } catch (Exception e) {
            e.printStackTrace();
            ////////////////////异常友好提示
            return;
        }
        if(tmp){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }else{
            ////////////////////异常友好提示
        }
    }
}
