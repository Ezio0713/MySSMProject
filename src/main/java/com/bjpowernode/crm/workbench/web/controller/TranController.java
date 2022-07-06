package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    DicValueService dicValueService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ContactsService contactsService;
@RequestMapping("workbench/transaction/index.do")
    public String index(HttpServletRequest request){
    List<DicValue> stage = dicValueService.queryDicValueByTypeCode("stage");
    List<DicValue> source = dicValueService.queryDicValueByTypeCode("source");
    List<DicValue> transactionType = dicValueService.queryDicValueByTypeCode("transactionType");
    request.setAttribute("stageList",stage);
    request.setAttribute("transactionTypeList",transactionType);
    request.setAttribute("sourceList",source);
    return "workbench/transaction/index";
}
//跳转保存页面
    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stage = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionType = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> source = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stage);
        request.setAttribute("transactionTypeList",transactionType);
        request.setAttribute("sourceList",source);
        return "workbench/transaction/save";
    }
    //根据阶段动态获取可能性
    @RequestMapping("workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        return possibility;
    }
    //动态获取客户
    @RequestMapping("workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String customerName){
        List<String> strings = customerService.queryCustomNameByName(customerName);
        return strings;
    }
    //动态获取联系人
    @RequestMapping("workbench/transaction/queryContactNameByName.do")
    @ResponseBody
    public Object queryContactNameByName(String contactName){
        List<Contacts> contacts = contactsService.queryContactNameByName(contactName);
        return contacts;
    }
    //保存交易
    @RequestMapping("workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(Map<String,Object> map, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        map.put("user",user);
        return 0;
    }
}
