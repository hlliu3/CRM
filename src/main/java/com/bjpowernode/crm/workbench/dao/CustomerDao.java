package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer selectOriginalCustomerByCompanyName(String companyName);

    int insertCustomer(Customer customer);

    List<Customer> selectCustomerNameByLikeSearch(String name);
}
