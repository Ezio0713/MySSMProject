package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.HSSFUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.mysql.cj.Session;
import com.sun.deploy.net.HttpResponse;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    //点击市场活动跳转页面
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        request.setAttribute("userList",users);
        return "workbench/activity/index";
    }
    //添加市场活动
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveActivity(Activity activity, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setCreateBy(user.getId());
        String uuid = UUIDUtils.getUUID();
        activity.setId(uuid);
        activity.setCreateTime(DateUtils.formateDate(new Date()));
        try {
            int i = activityService.saveActivity(activity);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试....");
        }
        return returnObject;
    }
    //查询所有市场活动
    @RequestMapping("workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    //这里我试过用Map接受传过来的参数，但是失败了，根本接收不到
    public Map queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,
                                               int pageNo,int pageSize){
        Map map = new HashMap();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        List<Activity> activities = activityService.queryAllActivity(map);
        int i = activityService.queryTotalActivity(map);
        Map map1 = new HashMap();
        map1.put("activityList",activities);
        map1.put("totalRows",i);
        return map1;
    }
    //删除市场活动
    @RequestMapping("workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivities(String[] id){
        System.out.println(id);
        ReturnObject returnObject = new ReturnObject();
        int i = activityService.deleteActivities(id);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage("删除成功！");
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
    //根据id查询市场活动
    @RequestMapping("workbench/activity/queryActivityById.do")
    @ResponseBody
    public Activity queryActivityById(String id){
        Activity activity = activityService.queryActivityByid(id);
        System.out.println(activity);
        return activity;
    }
    //更新市场活动
    @RequestMapping("workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        int i = activityService.updateActivityByid(activity);
        if (i>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setMessage("修改成功！");
        }else{
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试。。。");
        }
        return  returnObject;
    }
    //批量导出市场活动
    @RequestMapping("workbench/activity/exportAllActivitys.do")
    public void exportAllActivitys(HttpServletResponse response) throws IOException {
        int i=1;
        Map map = new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",999);
        List<Activity> activities = activityService.queryAllActivity(map);


        HSSFWorkbook wb =  new  HSSFWorkbook();
//创建HSSFSheet对象
        HSSFSheet sheet = wb.createSheet( "市场活动" );
//创建HSSFRow对象
        HSSFRow row = sheet.createRow( 0 );
//创建HSSFCell对象
        HSSFCell cell=row.createCell( 0 );
//设置单元格的值
        cell.setCellValue( "名称" );
        cell=row.createCell( 1 );
        cell.setCellValue( "所有者" );
        cell=row.createCell( 2 );
        cell.setCellValue( "开始日期" );
        cell=row.createCell( 3 );
        cell.setCellValue( "结束日期" );
        for (Activity activity:activities) {
            row = sheet.createRow(i);
            cell=row.createCell(0);
            cell.setCellValue(activity.getName());
            cell=row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell=row.createCell(2);
            cell.setCellValue(activity.getCreateTime());
            cell=row.createCell(3);
            cell.setCellValue(activity.getEndDate());
            i++;
        }
        //设置响应类型（excel表格）
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置响应头(设置附件方式处理（默认是直接在浏览器里写数据，写不了用浏览器直接打开，打不开再下载），设置文件名)
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //通过响应生成输出流
        OutputStream outputStream = response.getOutputStream();
        //写出excel文件对象
        wb.write(outputStream);
        //刷新输出流
        outputStream.flush();
        }
        //批量添加市场活动
    @RequestMapping("workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile ,HttpSession session){
        int changedPages=0;
        ReturnObject returnObject = new ReturnObject();
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        //由上传的文件创建输入流
        try {
            InputStream is=activityFile.getInputStream();

            //从输入流创建表格文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
//            InputStream is=activityFile.getInputStream();
//            HSSFWorkbook wb=new HSSFWorkbook(is);

        //获取文件第一页面
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        HSSFRow row = null;
        HSSFCell cell =null;
        int lastRowNum = sheet.getLastRowNum();
        for (int i=1;i<=lastRowNum;i++){
            Activity activity = new Activity();
            row=sheet.getRow(i);
            activity.setId(UUIDUtils.getUUID());
            activity.setCreateBy(user.getId());
            activity.setOwner(user.getId());
            activity.setCreateTime(DateUtils.formateDateTime(new Date()));
            for (int j=0;j<row.getLastCellNum();j++){
                cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String cellValueForStr = HSSFUtils.getCellValueForStr(cell);
                if(j==0){
                    activity.setName(cellValueForStr);
                }else if(j==1){
                    activity.setStartDate(cellValueForStr);
                }else if(j==2){
                    activity.setEndDate(cellValueForStr);
                }else if(j==3){
                    activity.setCost(cellValueForStr);
                }else if(j==4){
                    activity.setDescription(cellValueForStr);
                }

            }
            int pages = activityService.saveActivity(activity);
            changedPages += pages;
        }
        if (changedPages==sheet.getLastRowNum()+1){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(changedPages);
        }else if (changedPages<sheet.getLastRowNum()+1){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("未全部导入，已成功"+changedPages+"条数据");
        }
        } catch (IOException e) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试");
            e.printStackTrace();
        }
        return returnObject;
    }
    }
