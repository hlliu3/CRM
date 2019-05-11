package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.UserServiceImpl;
import com.bjpowernode.crm.utils.MyServlet;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends MyServlet {

    public void userLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        User user = new User();
        user.setLoginPwd(password);
        user.setLoginAct(username);

        User userRes = null;

        userRes = service.getUserByNameAndPw(user);

        if(userRes == null){
            request.setAttribute("errorinfo", 1);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        request.getSession().setAttribute("userInfo", userRes);
        response.sendRedirect(request.getContextPath()+"/workbench/index.jsp");
    }
}
