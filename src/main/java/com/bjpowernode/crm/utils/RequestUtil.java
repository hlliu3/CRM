package com.bjpowernode.crm.utils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;

public class RequestUtil {

    /*
     *  将当前请求协议包中请求参数内容自动赋值给一个对应的实体类对象
     *
     *  要求【请求参数名】必须与对应的实体类的【属性名】，完全一致（大小写都要一样）
     *
     */

    public static Object init(HttpServletRequest request,Class classFile){

        Field fieldArray[] = null;
        Enumeration paramNames=null;
        Object obj = null;
        //1.获得当前实体类所有的属性信息
        fieldArray = classFile.getDeclaredFields();
        //2.获得当前请求协议包所有的请求参数名
        paramNames = request.getParameterNames();
        //3.创建对应的实体类的实例对象
        try {
            obj = classFile.newInstance();
            //4.将请求参数内容依次赋值给实例对象属性
            while(paramNames.hasMoreElements()){

                String paramName = (String) paramNames.nextElement();
                String value     = request.getParameter(paramName);
                Field fieldObj = null;
                try{
                    fieldObj = classFile.getDeclaredField(paramName);
                }catch(NoSuchFieldException ex){
                    continue;
                }
                String typeName = fieldObj.getType().getName();
                fieldObj.setAccessible(true);
                if("java.lang.String".equals(typeName)){
                    fieldObj.set(obj, value);
                }else if("java.lang.Integer".equals(typeName)){
                    fieldObj.set(obj, Integer.valueOf(value));
                }else if("java.lang.Double".equals(typeName)){
                    fieldObj.set(obj, Double.valueOf(value));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return obj;
    }

}




