package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.workbench.domain.Activity;
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
    public boolean insertActive(Activity activity);

    public PageDataVO<Activity> selectActivityPageData(Map<String, Object> map);

    public boolean deleteActivityBath(String[] ids);

    public HashMap<String,Object> selectActivityById(String id);

    public boolean updateActivity(Activity activity);

}
