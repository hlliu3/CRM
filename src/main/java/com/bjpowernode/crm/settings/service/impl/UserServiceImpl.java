package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/11  17:27
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = null;

    @Override
    public User getUserByNameAndPw(User user,String ip) throws LoginException {
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        User userRes = userDao.getUserByNameAndPw(user);
        if(userRes == null){//没有找到
            throw new LoginException("用户名或密码错误");//注意这个异常是要抛到代理类中的，由代理类继续向上抛到controller中
        }
        if(userRes.getExpireTime()!=null&&!("".equals(userRes.getExpireTime()))) {//如果有登录限制时间
            String currentTime = DateTimeUtil.getSysTime();
            if (currentTime.compareTo(userRes.getExpireTime()) < 0) {//可以登录
                throw new LoginException("用户时间已经失效，请联系管理员");
            }
        }
        if(userRes.getLockState()!=null&&!("".equals(userRes.getLockState()))) {
            if ("0".equals(userRes.getLockState())) {//锁定状态
                throw new LoginException("用户被锁定，请联系管理员");
            }
        }
        if(userRes.getAllowIps()!=null&&!("".equals(userRes.getAllowIps()))){//有限制的ip
            if(userRes.getAllowIps().contains(ip)){//不允许登录的ip
                throw new LoginException("用户ip受限，请联系管理员");
            }
        }
        return userRes;
    }

    public List<User> getUserList(){
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        List<User> list = null;
        list = userDao.getUserList();
        return list;
    }
}
