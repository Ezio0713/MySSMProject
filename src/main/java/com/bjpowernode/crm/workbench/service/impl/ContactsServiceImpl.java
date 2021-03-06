package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    ContactsMapper contactsMapper;
    @Override
    public int insert(Contacts row) {
        int insert = contactsMapper.insert(row);
        return insert;
    }

    @Override
    public List<Contacts> queryContactNameByName(String fullName) {
        List<Contacts> contacts = contactsMapper.queryContactNameByName(fullName);
        return contacts;
    }
}
