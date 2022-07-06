package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.mapper.ContactsRemarkMapper;
import com.bjpowernode.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Autowired
    ContactsRemarkMapper contactsRemarkMapper;
    @Override
    public int insert(ContactsRemark row) {
        int insert = contactsRemarkMapper.insert(row);
        return insert;
    }
}
