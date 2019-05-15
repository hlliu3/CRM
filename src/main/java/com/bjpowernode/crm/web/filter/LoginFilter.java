package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = (User) request.getSession().getAttribute("userInfo");
        String servletPath = request.getServletPath();
        if("/login.jsp".equals(servletPath)||"/user/userLogin.do".equals(servletPath)){
            chain.doFilter(req, resp);
        }else if(user==null){//校验session
            response.sendRedirect(request.getContextPath()+"/login.jsp");//跳转到登录页面
        }else{
            chain.doFilter(req, resp);
        }

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
