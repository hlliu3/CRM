package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public interface UserDao {
    public User getUserByNameAndPw(User user);
}
