package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.datadictionary.service.DicService;
import com.bjpowernode.crm.datadictionary.service.serviceimpl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  21:08
 */
public class DataDictionaryCache implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {//创建application域时执行
        System.out.println("开始数据字典缓存");
        ServletContext application = sce.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, Object> map = dicService.selectDicValueByType();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            application.setAttribute(entry.getKey(), entry.getValue());
        }

        System.out.println("结束数据字典缓存");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
