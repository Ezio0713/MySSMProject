package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    //保存线索备注
    int saveClueDetail(ClueRemark clueRemark);
    List<ClueRemark> queryClueRemarkByClueId(String clueId);
    int deleteClueRemarkByClueId(String id);
    int updateClueRemark(ClueRemark clueRemark);
    ClueRemark selectClueRemarkById(String id);
}
