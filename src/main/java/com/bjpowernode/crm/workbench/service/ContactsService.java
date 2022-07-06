package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    int insert(Contacts row);
    List<Contacts> queryContactNameByName(String fullName);
}
