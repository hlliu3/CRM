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
    public int insertActive(Activity activity);
    public int selectCountActivity(Map<String,Object> map);
    public List<Activity> selectActivityPageData(Map<String,Object> map);
    public int deleteActivityBath(String[] ids);
    public int deleteActivityBathCount(String[] ids);
    public Activity selectActivityById(String id);
    public int updateActivity(Activity activity);
}
