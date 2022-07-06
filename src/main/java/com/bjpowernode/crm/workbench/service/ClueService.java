package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;
import java.util.Map;

public interface ClueService {
    //创建明细
    public int saveClue(Clue clue);
    public List<Clue> queryClueByConditionOnPages(Map<String,Object> map);
    public int queryClueCountByConditionOnPages(Map<String,Object> map);
    public Clue queryClueById(String id);
    void saveConvertClue(Map<String,Object> map);
}
