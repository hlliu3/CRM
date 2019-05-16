package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/16  22:39
 */
public interface ClueService {
    List<User> selectUserList();
}
