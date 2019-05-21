package com.bjpowernode.crm.workbench.serivce.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.serivce.CustomerService;
import com.bjpowernode.crm.workbench.serivce.TransactionService;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/18  19:34
 */
public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public List<Customer> selectCustomerNameByLikeSearch(String name) {
        List<Customer> customerList = customerDao.selectCustomerNameByLikeSearch(name);
        return customerList;
    }
}
