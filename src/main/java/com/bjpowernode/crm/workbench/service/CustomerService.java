package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    int saveCustomer(Customer row);
    List<Customer> queryAllByConditionsForPage(Map<String,Object> map);
    int queryCountByConditionsForPage(Map<String,Object> map);
    List<String> queryCustomNameByName(String name);
}
