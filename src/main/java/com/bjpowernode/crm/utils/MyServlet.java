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
        String sp = request.getServletPath();
        //获取请求地址的userLogin.do的userLoging，把userLogin当做方法名
        String methodName = sp.substring(sp.lastIndexOf("/"),sp.lastIndexOf(".")).replaceAll("/", "");

        Class clazz = this.getClass();//this代表的是具体的servlet类，继承了MyServlet的类

        try {
            //获取方法，根据方法名和方法参数类型
            Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
            method.invoke(this, request,response);//方法调用
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
