package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;
    //打开市场活动明细
    @RequestMapping("workbench/activity/detailActivity.do")
    public String  detailActivity(String id, HttpServletRequest request){
        Activity activity = activityService.queryActivityByid(id);
        User user = userService.queryUserById(activity.getCreateBy());
        activity.setCreateBy(user.getName());
        if (activity.getEditBy()!=null&&activity.getEditBy()!=""){
            User editUser = userService.queryUserById(activity.getEditBy());
            activity.setEditBy(editUser.getName());
        }
        //remarkList
        List<ActivityRemark> activityRemarks = activityRemarkService.queryActivityRemarkByActivityId(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",activityRemarks);
        return "workbench/activity/detail";
    }
    //保存市场活动明细
    @RequestMapping("workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(HttpSession session,String noteContent,String activityId){
        ActivityRemark activityRemark = new ActivityRemark();
        ReturnObject returnObject = new ReturnObject();
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setActivityId(activityId);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        User user =(User)session.getAttribute(Contants.SESSION_USER);
        activityRemark.setCreateBy(user.getId());
        int i = activityRemarkService.addActivityRemark(activityRemark);
        if (i==1){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(activityRemark);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试");
        }
        return returnObject;
    }
    //删除市场活动明细
    @RequestMapping("workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        int i = activityRemarkService.deleteActivityRemarkById(id);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
    //修改市场活动明细
    @RequestMapping("workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark activityRemark,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user =(User) session.getAttribute(Contants.SESSION_USER);
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);
        activityRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        int i = activityRemarkService.updateActivityRemarkById(activityRemark);
        if (i>0){
         returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
}
