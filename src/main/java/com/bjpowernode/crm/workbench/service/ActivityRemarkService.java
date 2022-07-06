package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ActivityRemarkService {
    int addActivityRemark(ActivityRemark activityRemark);
    List<ActivityRemark> queryActivityRemarkByActivityId(String activityId);
    int deleteActivityRemarkById(String activityRemarkId);
    int updateActivityRemarkById(ActivityRemark activityRemark);
}
