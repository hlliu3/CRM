package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.datadictionary.service.DicService;
import com.bjpowernode.crm.datadictionary.service.serviceimpl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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

        //进行阶段对应的可能性参数进行缓存properties文件解析Stage2Possibility.properties
        Map<String,String> statusForRateMap = new HashMap<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            statusForRateMap.put(key, value);
        }
        application.setAttribute("statusForRateMap", statusForRateMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
