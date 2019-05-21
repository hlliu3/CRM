package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.datadictionary.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MyServlet;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.serivce.CustomerService;
import com.bjpowernode.crm.workbench.serivce.TransactionService;
import com.bjpowernode.crm.workbench.serivce.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.serivce.impl.TransactionServiceImpl;

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
 * date:2019/5/18  19:33
 */
public class TransactionServlet extends MyServlet {
    public void getCustomerNameByLikeSearch(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<Customer> customerList = customerService.selectCustomerNameByLikeSearch(name);
        List<String> list = new ArrayList<>();
        for (Customer customer : customerList) {
            list.add(customer.getName());
        }
        PrintJson.printJsonObj(response, list);
    }

    public void selectTransactionHsitory(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<TranHistory> tranHistories = transactionService.selectTransactionHsitory(id);
        Map<String,String> statusForRateMap = (Map<String, String>) request.getServletContext().getAttribute("statusForRateMap");
        for (TranHistory tranHistory : tranHistories) {

            tranHistory.setProbability(statusForRateMap.get(tranHistory.getStage()));
        }
        PrintJson.printJsonObj(response, tranHistories);

    }

    public void getUserListForShowCreatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        List<User> users = transactionService.selectUserList();
        request.setAttribute("userList", users);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }

    public void selectTransactionById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        Tran tran = transactionService.selectTransactionById((String)request.getParameter("id"));
        Map<String,String> map = (Map<String, String>) request.getServletContext().getAttribute("statusForRateMap");
        tran.setProbability(map.get(tran.getStage()));
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setCreateBy(((User)request.getSession().getAttribute("userInfo")).getName());
        request.setAttribute("tran", tran);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    public void updateStageForTransaction(HttpServletRequest request, HttpServletResponse response){
        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        Map<String,String> map = new HashMap<>();
        map.put("id", id);
        map.put("stage",stage);
        map.put("editBy",((User)(request.getSession().getAttribute("userInfo"))).getName());
        map.put("editTime",DateTimeUtil.getSysTime());
        map.put("createBy",((User)(request.getSession().getAttribute("userInfo"))).getName());
        map.put("expectedDate", request.getParameter("expectedDate"));
        map.put("money", request.getParameter("money"));
        map.put("probability",((Map<String,String>)request.getServletContext().getAttribute("statusForRateMap")).get(stage));

        TransactionService transactionService = (TransactionService) ServiceFactory.getService(new TransactionServiceImpl());
        boolean flag = transactionService.updateStageForTransaction(map);


        Map<String,Object> resMap = new HashMap<>();
        resMap.put("success", flag);
        resMap.put("transaction",map);
        resMap.put("stages",(List<DicValue>)request.getServletContext().getAttribute("stage"));
        PrintJson.printJsonObj(response,resMap);

    }
}
