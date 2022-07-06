package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    ClueActivityRelationMapper clueActivityRelationMapper;
    @Override
    public int insert(ClueActivityRelation row) {
        int insert = clueActivityRelationMapper.insert(row);
        return insert;
    }

    @Override
    public int deleteByClueIdAndActivityId(Map<String, Object> map) {
        int i = clueActivityRelationMapper.deleteByClueIdAndActivityId(map);
        return i;
    }

    @Override
    public String queryRelationByClueIdAndActivityId(Map<String, Object> map) {
        String s = clueActivityRelationMapper.queryRelationByClueIdAndActivityId(map);
        return s;
    }

    @Override
    public int deleteByClueId(String clueId) {
        int i = clueActivityRelationMapper.deleteByClueId(clueId);
        return i;
    }
}
