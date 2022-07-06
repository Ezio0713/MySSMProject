package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
    TranMapper tranMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Override
    public int saveCreateTran(Map<String, Object> map) {
        User user = (User) map.get("user");
        String customerName = (String) map.get("customerName");
        Customer customer;
        if (customerMapper.queryCustomerByName(customerName) == null){
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customer.setName(customerName);
            customerMapper.insert(customer);
        }else {
            customer = customerMapper.queryCustomerByName(customerName);
        }
        Tran tran = new Tran();
        tran.setOwner((String)map.get("owner"));
        tran.setMoney((String)map.get("=money"));
        tran.setName((String)map.get("name"));
        tran.setExpectedDate((String)map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setStage((String)map.get("stage"));
        tran.setType((String)map.get("type"));
        tran.setSource((String)map.get("source"));
        tran.setActivityId((String)map.get("activityId"));
        return 0;
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        List<FunnelVO> funnelVOS = tranMapper.queryCountOfTranGroupByStage();
        return funnelVOS;
    }
}
