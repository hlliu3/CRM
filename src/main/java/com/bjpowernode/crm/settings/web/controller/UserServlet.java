package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class UserServlet extends MyServlet {

    public void userLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        User user = new User();
        String md5PW = MD5Util.getMD5(password);
        user.setLoginPwd(md5PW);
        user.setLoginAct(username);

        User userRes = null;
        HashMap<String,Object> hashMap = new HashMap<>();
        try {
            userRes = service.getUserByNameAndPw(user,request.getRemoteAddr());
        } catch (LoginException e) {//接受处理异常
            e.printStackTrace();

            hashMap.put("success", false);
            hashMap.put("msg",e.getMessage());
            PrintJson.printJsonObj(response, hashMap);//向响应流中放入数据，并响应
            return;
        }
        //执行到这里说明登录成功
        request.getSession().setAttribute("userInfo", userRes);
        PrintJson.printJsonFlag(response, true);//向响应流中放入数据，并响应
    }
}
