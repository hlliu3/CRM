package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ActiveDao;
import com.bjpowernode.crm.workbench.dao.ActiveRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.PageDataVO;
import com.bjpowernode.crm.workbench.serivce.ActiveService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  19:29
 */
public class ActiveServiceImpl implements ActiveService {
    private ActiveDao activeDao = null;
    private ActiveRemarkDao activeRemarkDao = null;
    private UserDao userDao = null;
    @Override
    public boolean insertActive(Activity activity){
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        int flag ;

        try {
            flag = activeDao.insertActive(activity);//异常
            if(flag==0){
               return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        int flagActivity ;
        int flagActivityRemark ;
        int deleteActivityCount ;
        int deleteActivityRemarkCount ;

        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);

        flagActivity = activeDao.deleteActivityBathCount(ids);
        flagActivityRemark = activeRemarkDao.deleteActivityBathOfRemarkCount(ids);

        deleteActivityRemarkCount = activeRemarkDao.deleteActivityBathOfRemark(ids);
        try {
            if(deleteActivityRemarkCount != flagActivityRemark){
                return false;
            }
            deleteActivityCount = activeDao.deleteActivityBath(ids);
            if(deleteActivityCount != flagActivity){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public HashMap<String,Object> selectActivityById(String id) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

        Activity activity ;
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
        int flag;
        try {
            flag = activeDao.updateActivity(activity);
            if(flag!=1){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public Activity activityDetail(String activityId) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        Activity activity = activeDao.selectactivityDetailById(activityId);
        return activity;
    }

    @Override
    public List<ActivityRemark> selectRemarkByActivityId(String id) {
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);
        List<ActivityRemark> activityRemarks = activeRemarkDao.selectRemarkByActivityId(id);

        return activityRemarks;
    }

    @Override
    public boolean deleteActivityRemarkByRemarkId(String id) {
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);
        int flag = 0;
        flag = activeRemarkDao.deleteActivityRemarkByRemarkId(id);
        return flag==0?false:true;

    }

    @Override
    public boolean updateActivityRemarkByRemarkId(ActivityRemark activityRemark) {
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);
        int flag = 0;
        flag = activeRemarkDao.updateActivityRemarkByRemarkId(activityRemark);

       return flag==0?false:true;
    }

    @Override
    public boolean insertRemark(ActivityRemark activityRemark) {
        activeRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActiveRemarkDao.class);
        int flag = 0;
        flag = activeRemarkDao.insertRemark(activityRemark);
        return flag==0?false:true;
    }


    @Override
    public List<Activity> selectActivityNotByClueId(Map<String,Object> map) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        List<Activity> activities = activeDao.selectActivityNotByClueId(map);
        return activities;
    }

    @Override
    public List<Activity> selectActivityListByClueId(Map<String, Object> map) {
        activeDao = SqlSessionUtil.getSqlSession().getMapper(ActiveDao.class);
        List<Activity> activities = activeDao.selectActivityListByClueId(map);
        return activities;
    }
}
