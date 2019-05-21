package com.bjpowernode.crm.workbench.serivce;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/18  19:33
 */
public interface CustomerService {
    List<Customer> selectCustomerNameByLikeSearch(String name);
}
