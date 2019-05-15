package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public interface UserDao {
    public User getUserByNameAndPw(User user);
    public List<User> getUserList();
}
