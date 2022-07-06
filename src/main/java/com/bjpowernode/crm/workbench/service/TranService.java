package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FunnelVO;

import java.util.List;
import java.util.Map;

public interface TranService {
    int saveCreateTran(Map<String,Object> map);
     List<FunnelVO> queryCountOfTranGroupByStage();
}
