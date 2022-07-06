package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueRemarkController {
    @Autowired
    ActivityService activityService;
    @Autowired
    ClueRemarkService clueRemarkService;
    @Autowired
    ClueActivityRelationService clueActivityRelationService;
    //保存线索明细
    @RequestMapping("workbench/clue/saveCreateClueRemark.do")
    @ResponseBody
    public Object saveCreateClueRemark(String noteContent, String clueId, HttpSession session){
        System.out.println("这是clueid"+clueId);
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setClueId(clueId);
        clueRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        clueRemark.setNoteContent(noteContent);
        clueRemark.setId(UUIDUtils.getUUID());
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        clueRemark.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        int i = clueRemarkService.saveClueDetail(clueRemark);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(clueRemark);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙");
        }
        return returnObject;
    }
    //删除线索备注
    @RequestMapping("workbench/clue/deleteClueRemarkById.do")
    @ResponseBody
    public Object deleteClueRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        int i = clueRemarkService.deleteClueRemarkByClueId(id);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
    //修改线索备注
    @RequestMapping("workbench/clue/saveEditClueRemark.do")
    @ResponseBody
    public Object updateClueRemarkByID(String id,String noteContent,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        ClueRemark clueRemark = clueRemarkService.selectClueRemarkById(id);
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        clueRemark.setEditBy(user.getId());
        clueRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        clueRemark.setNoteContent(noteContent);
        clueRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);
        int i = clueRemarkService.updateClueRemark(clueRemark);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
    //根据名称和线索id查询未关联市场活动
    @RequestMapping("workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("name",activityName);
        map.put("clueId",clueId);
        List<Activity> activities = activityService.queryActivityByActivityName(map);
        return activities;
    }
    //根据名称和线索id查询已关联的市场活动
    @RequestMapping("workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("name",activityName);
        map.put("clueId",clueId);
        List<Activity> activities = activityService.queryActivityForConvertByNameClueId(map);
        return activities;
    }
    //保存市场活动关联
    @RequestMapping("workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){

        ReturnObject returnObject = new ReturnObject();
        List<Activity> relations = new ArrayList<>();
        int count=0;
        for (String activityid:activityId) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtils.getUUID());
            clueActivityRelation.setActivityId(activityid);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelationService.insert(clueActivityRelation);
            count++;
            Activity activity = activityService.queryActivityByid(activityid);
            relations.add(activity);
        }
        if (count == activityId.length){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(relations);
            returnObject.setMessage("保存成功！");
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("稍后再试");
        }
        return returnObject;
    }
    //解除关联
    @RequestMapping("workbench/clue/saveUnbund.do")
    @ResponseBody
    public Object saveUnbund(String activityId,String clueId){
        ReturnObject returnObject = new ReturnObject();
        Map<String,Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("clueId",clueId);
        int i = clueActivityRelationService.deleteByClueIdAndActivityId(map);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;

    }
}
