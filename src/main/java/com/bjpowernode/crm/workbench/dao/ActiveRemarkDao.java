package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/14  18:53
 */
public interface ActiveRemarkDao {
    int deleteActivityBathOfRemark(String[] activityIds);
    int deleteActivityBathOfRemarkCount(String[] activityIds);
    List<ActivityRemark> selectRemarkByActivityId(String id);
    int deleteActivityRemarkByRemarkId(String id);
    int updateActivityRemarkByRemarkId(ActivityRemark activityRemark);
    int insertRemark(ActivityRemark activityRemark);
}
