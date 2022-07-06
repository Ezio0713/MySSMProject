package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.mapper.CustomerRemarkMapper;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    CustomerRemarkMapper customerRemarkMapper;
    @Override
    public int insert(CustomerRemark row) {
        int insert = customerRemarkMapper.insert(row);
        return insert;
    }
}
