package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/18  19:33
 */
public interface TransactionService {
    List<TranHistory> selectTransactionHsitory(String id);

    List<User> selectUserList();

    Tran selectTransactionById(String id);

    boolean updateStageForTransaction(Map<String, String> map);

    Map<String, Object> selectTransactionForChart();
}
