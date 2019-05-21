package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.PageDataVO;

import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  19:24
 */
public interface ActiveDao {
    int insertActive(Activity activity);
    int selectCountActivity(Map<String,Object> map);
    List<Activity> selectActivityPageData(Map<String,Object> map);
    int deleteActivityBath(String[] ids);
    int deleteActivityBathCount(String[] ids);
    Activity selectActivityById(String id);
    int updateActivity(Activity activity);

    Activity selectactivityDetailById(String activityId);
    List<Activity> selectActivityNotByClueId(Map<String,Object> map);
    List<Activity> selectActivityListByClueId(Map<String,Object> map);

    List<Activity> selectActivityByClueIdByName(Map<String,Object> map);

}
