package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.Map;

public interface ClueActivityRelationService {
    int insert(ClueActivityRelation row);
    int deleteByClueIdAndActivityId(Map<String,Object> map);
    String queryRelationByClueIdAndActivityId(Map<String,Object> map);
    int deleteByClueId(String clueId);
}
