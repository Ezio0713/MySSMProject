package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    ActivityRemarkMapper activityRemarkMapper;
    @Override
    public int addActivityRemark(ActivityRemark activityRemark) {
        int insert = activityRemarkMapper.insert(activityRemark);
        return insert;
    }
//获取所有活动评价
    @Override
    public List<ActivityRemark> queryActivityRemarkByActivityId(String activityId) {
        List<ActivityRemark> activityRemarks = activityRemarkMapper.queryActivityRemarkByActivityId(activityId);
        return activityRemarks;
    }

    @Override
    public int deleteActivityRemarkById(String activityRemarkId) {
        int i = activityRemarkMapper.deleteByPrimaryKey(activityRemarkId);
        return i;
    }

    @Override
    public int updateActivityRemarkById(ActivityRemark activityRemark) {
        int i = activityRemarkMapper.updateByPrimaryKeySelective(activityRemark);
        return i;
    }
}
