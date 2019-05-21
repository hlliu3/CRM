package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.serivce.TransactionService;

import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/18  19:34
 */
public class TransactionServiceImpl implements TransactionService {
    TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public List<TranHistory> selectTransactionHsitory(String id) {
        List<TranHistory> tranHistories = tranHistoryDao.selectTransactionHsitory(id);

        return tranHistories;
    }

    @Override
    public List<User> selectUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }

    @Override
    public Tran selectTransactionById(String id) {
        Tran tran = tranDao.selectTransactionById(id);
        return tran;
    }

    @Override
    public boolean updateStageForTransaction(Map<String, String> map) {
        boolean flag = false;
        if(tranDao.updateTransaction(map)==1){
            flag = true;
        }
        //创建交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setTranId(map.get("id"));
        tranHistory.setCreateBy(map.get("createBy"));
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(map.get("expectedDate"));
        tranHistory.setMoney(map.get("money"));
        tranHistory.setStage(map.get("stage"));

        if(!(tranHistoryDao.insertTranHistory(tranHistory)==1)){
            flag = false;
        }
        return flag;
    }
}
