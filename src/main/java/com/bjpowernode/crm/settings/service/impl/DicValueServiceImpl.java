package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.mapper.DicValueMapper;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    DicValueMapper dicValueMapper;
    @Override
    public List<DicValue> queryDicValueByTypeCode(String TypeCode) {
        List<DicValue> dicValues = dicValueMapper.selectDicByTypeCode(TypeCode);
        return dicValues;
    }
}
