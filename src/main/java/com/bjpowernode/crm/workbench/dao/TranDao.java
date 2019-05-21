package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.Map;

public interface TranDao {

    int insertTran(Tran tran);
    Tran selectTransactionById(String id);

    int updateTransaction(Map<String, String> map);
}
