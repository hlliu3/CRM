package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.serivce.ClueService;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  22:39
 */
public class ClueServiceImpl implements ClueService {
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public List<User> selectUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }
}
