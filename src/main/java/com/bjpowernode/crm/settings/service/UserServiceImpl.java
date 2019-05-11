package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public class UserServiceImpl implements UserService{
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User getUserByNameAndPw(User user) {
        return userDao.getUserByNameAndPw(user);
    }
}
