package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.Utils.UUIDUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
   private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CustomerService customerService;
    //点击线索跳转页面
    @RequestMapping("workbench/clue/index.do")
    public String index(HttpServletRequest request){
        request.setAttribute("userList",userService.queryAllUsers());
        request.setAttribute("appellationList",dicValueService.queryDicValueByTypeCode("appellation"));
        request.setAttribute("clueStateList",dicValueService.queryDicValueByTypeCode("clueState"));
        request.setAttribute("sourceList",dicValueService.queryDicValueByTypeCode("source"));
        return "workbench/clue/index";
    }
    //保存线索
    @RequestMapping("workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        System.out.println( clue.getOwner()+"这里这里中！！！！！！！");
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        int insertFlag = clueService.saveClue(clue);
        if (insertFlag>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，稍后再试");
        }
        return returnObject;
    }
    //查询所有线索
    @RequestMapping("workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Map queryClueByConditionForPage(String fullname, String owner, String company, String phone,
                                              String mphone, String source, String state,int pageNo,int pageSize){
        Map<String,Object> map = new HashMap();
        map.put("fullname",fullname);
        map.put("owner", owner);
        map.put("company",company);
        System.out.println(company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("state",state);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        List<Clue> clueList = clueService.queryClueByConditionOnPages(map);
        int i = clueService.queryClueCountByConditionOnPages(map);
        Map map1 = new HashMap();
        map1.put("clueList",clueList);
        map1.put("totalRows",i);
        return map1;
    }
    //跳转线索明细
    @RequestMapping("workbench/clue/detailClue.do")
    public String detailClue(@RequestParam(value = "id") String id,HttpServletRequest request){
        Clue clue = clueService.queryClueById(id);
        System.out.println(id);
        List<ClueRemark> clueRemarks = clueRemarkService.queryClueRemarkByClueId(id);
        List<Activity> activities = activityService.queryActivityByClueId(id);
        request.setAttribute("activityList",activities);
        request.setAttribute("remarkList",clueRemarks);
        User user = userService.queryUserById(clue.getOwner());
        clue.setOwner(user.getName());
        request.setAttribute("clue",clue);
        return "workbench/clue/detail";
    }
    //跳转转换页面
    @RequestMapping("workbench/clue/toConvert.do")
    public String toConvert(@RequestParam(value = "id") String id,HttpServletRequest request){
        List<DicValue> stage = dicValueService.queryDicValueByTypeCode("stage");
        Clue clue = clueService.queryClueById(id);
        User user = userService.queryUserById(clue.getOwner());
        clue.setOwner(user.getName());
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stage);
        return "workbench/clue/convert";
    }
    //转换线索
    @RequestMapping("workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId, String money, String name, String expectedDate, String stage,
                              String activityId, String isCreateTran,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User)session.getAttribute(Contants.SESSION_USER);
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name", name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put("user",user);
            try {
                clueService.saveConvertClue(map);
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }catch(Exception e){
                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }

            return returnObject;
        }
}
