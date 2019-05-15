package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public interface UserService {
    /**
     * @param user
     * @return
     * @throws LoginException
     */
    public User getUserByNameAndPw(User user,String ip) throws LoginException;
    public List<User> getUserList();
}
