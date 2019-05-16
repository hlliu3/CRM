package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.workbench.dao.ActiveRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.PageDataVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/13  19:28
 */
public interface ActiveService {
    boolean insertActive(Activity activity);

    PageDataVO<Activity> selectActivityPageData(Map<String, Object> map);

    boolean deleteActivityBath(String[] ids);

    HashMap<String,Object> selectActivityById(String id);

    boolean updateActivity(Activity activity);

    Activity activityDetail(String activityId);

    List<ActivityRemark> selectRemarkByActivityId(String id);

    boolean deleteActivityRemarkByRemarkId(String id);

    boolean updateActivityRemarkByRemarkId(ActivityRemark activityRemark);

    boolean insertRemark(ActivityRemark activityRemark);
}
