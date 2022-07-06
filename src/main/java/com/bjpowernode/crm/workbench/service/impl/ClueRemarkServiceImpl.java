package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    @Override
    public int saveClueDetail(ClueRemark clueRemark) {
        int insert = clueRemarkMapper.insert(clueRemark);
        return insert;
    }

    @Override
    public List<ClueRemark> queryClueRemarkByClueId(String clueId) {
        List<ClueRemark> clueRemarks = clueRemarkMapper.queryClueRemarkByClueId(clueId);
        return clueRemarks;
    }

    @Override
    public int deleteClueRemarkByClueId(String id) {
        int i = clueRemarkMapper.deletClueRemarkById(id);
        return i;
    }

    @Override
    public int updateClueRemark(ClueRemark clueRemark) {
        int i = clueRemarkMapper.updateClueRemarkById(clueRemark);
        return i;
    }

    @Override
    public ClueRemark selectClueRemarkById(String id) {
        ClueRemark clueRemark = clueRemarkMapper.selectByPrimaryKey(id);
        return clueRemark;
    }
}
