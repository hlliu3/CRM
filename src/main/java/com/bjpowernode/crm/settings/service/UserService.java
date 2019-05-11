package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public interface UserService {
    public User getUserByNameAndPw(User user);
}
