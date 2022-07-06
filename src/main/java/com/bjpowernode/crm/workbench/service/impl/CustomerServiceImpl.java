package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerMapper customerMapper;
    @Override
    public int saveCustomer(Customer row) {
        int insert = customerMapper.insert(row);
        return insert;
    }

    @Override
    public List<Customer> queryAllByConditionsForPage(Map<String, Object> map) {
        List<Customer> customers = customerMapper.queryAllByConditionsForPage(map);
        return customers;
    }

    @Override
    public int queryCountByConditionsForPage(Map<String, Object> map) {
        int i = customerMapper.queryCountByConditionsForPage(map);
        return i;
    }
    @Override
    public List<String> queryCustomNameByName(String name) {
        List<String> strings = customerMapper.queryCustomNameByName(name);
        return strings;
    }
}
