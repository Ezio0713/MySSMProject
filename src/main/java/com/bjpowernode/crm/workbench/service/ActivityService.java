package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveActivity(Activity activity);
    List<Activity> queryAllActivity(Map<String,Object> map);
    int queryTotalActivity(Map<String,Object> map);
    int deleteActivities(String[] ids);
    Activity queryActivityByid(String id);
     int updateActivityByid(Activity activity);
    List<Activity> queryActivityByClueId(String ClueId);
    List<Activity> queryActivityByActivityName(Map<String,Object> map);
    List<Activity> queryActivityForConvertByNameClueId(Map<String,Object> map);
}
