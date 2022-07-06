package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    ActivityMapper activityMapper;
    public int saveActivity(Activity activity){
        int insert = activityMapper.insert(activity);
        return insert;
    }
    //返回所有市场活动列表
    public List<Activity> queryAllActivity(Map<String,Object> map){
        List<Activity> activities = activityMapper.queryAllByConditionsForPage(map);
        return activities;
    }
//返回所有市场活动总条数
    @Override
    public int queryTotalActivity(Map<String,Object> map) {
        int i = activityMapper.querytotalCountByConditionsForPage(map);
        return i;
    }

    @Override
    public int deleteActivities(String[] ids) {
        int i = activityMapper.deleteByPrimaryKey(ids);
        return i;
    }
    public Activity queryActivityByid(String id){
        Map<String,Object> map = new HashMap<>();
        Activity activity = activityMapper.selectByPrimaryKey(id);
        return activity;
    }
//修改市场活动
    @Override
    public int updateActivityByid(Activity activity) {
        int i = activityMapper.updateByPrimaryKey(activity);
        return i;
    }

    @Override
    public List<Activity> queryActivityByClueId(String ClueId) {
        List<Activity> activities = activityMapper.queryActivityByClueId(ClueId);
        return activities;
    }

    @Override
    public List<Activity> queryActivityByActivityName(Map<String,Object> map) {
        List<Activity> activities = activityMapper.queryActivityByActivityName(map);
        return activities;
    }

    @Override
    public List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map) {
        List<Activity> activities = activityMapper.queryActivityForConvertByNameClueId(map);
        return activities;
    }
}
