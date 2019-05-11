package com.bjpowernode.crm.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/922:38
 */
public class MyServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=utf8");

        String methodName = request.getServletPath().substring(request.getServletPath().lastIndexOf("/")).replace("/", "");

        Class t = this.getClass();

        try {
            Method method = t.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
            String rs = (String) method.invoke(this, request,response);
            System.out.println(rs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
