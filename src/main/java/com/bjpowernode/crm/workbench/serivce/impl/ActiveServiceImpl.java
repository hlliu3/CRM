package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ActiveDao;
import com.bjpowernode.crm.workbench.dao.ActiveRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.PageDataVO;
import com.bjpowernode.crm.workbench.serivce.ActiveService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  19:29
 */
public class ActiveServiceImpl implements ActiveService {
    ActiveDao activeDao = null;
    ActiveRemarkDao activeRemarkDao = null;
    UserDao userDao = null;
    public boolean insertActive(Activity activity){
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        int flag = 0;

        flag = activeDao.insertActive(activity);//异常

        if(flag>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public PageDataVO<Activity> selectActivityPageData(Map<String, Object> map) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        int total = activeDao.selectCountActivity(map);
        List<Activity> activitys = activeDao.selectActivityPageData(map);
        PageDataVO<Activity> pageDataVO = new PageDataVO<>();
        pageDataVO.setTotal(total);
        pageDataVO.setDatalist(activitys);
        return pageDataVO;
    }

    @Override
    public boolean deleteActivityBath(String[] ids) {
        int flagActivity = 0;
        int flagActivityRemark = 0;
        int deleteActivityCount = 0;
        int deleteActivityRemarkCount = 0;

        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);

        flagActivity = activeDao.deleteActivityBathCount(ids);
        flagActivityRemark = activeRemarkDao.deleteActivityBathOfRemarkCount(ids);

        deleteActivityRemarkCount = activeRemarkDao.deleteActivityBathOfRemark(ids);
        try {
            if(deleteActivityRemarkCount != flagActivityRemark){
                throw new RuntimeException("删除remark失败");
            }
            deleteActivityCount = activeDao.deleteActivityBath(ids);
            if(deleteActivityCount != flagActivity){
                throw new RuntimeException("删除activity失败");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public HashMap<String,Object> selectActivityById(String id) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

        Activity activity = null;
        activity = activeDao.selectActivityById(id);
        List<User> userList = userDao.getUserList();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userList", userList);
        hashMap.put("activity", activity);
        return hashMap;
    }

    @Override
    public boolean updateActivity(Activity activity) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        int flag = activeDao.updateActivity(activity);
        if(flag==1){
            return true;
        }else{
            return false;
        }

    }

}
