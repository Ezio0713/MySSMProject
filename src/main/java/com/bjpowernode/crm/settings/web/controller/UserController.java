package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.Utils.DateUtils;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    //从index页面跳转到登录页面（再index.jsp页面做了请求转发）
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "/settings/qx/user/login";
    }
    @RequestMapping("settings/qx/user/login.do")
    @ResponseBody
    public Object Login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        ReturnObject returnObject = new ReturnObject();
        User user = userService.queryUserByLoginActAndPwd(map);
        System.out.println(user);
        if (user==null){
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("该用户不存在");
           return returnObject;
        }
        if (!user.getLoginPwd().equals(loginPwd)){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("密码错误");
            return returnObject;
        }
        if(DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime())>0){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("该账户已过期");
            return returnObject;
        }
        if("0".equals(user.getLockState())){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("该账户已被锁定");
            return returnObject;
        }
        if (!(user.getAllowIps().contains(request.getRemoteAddr()))){
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("ip地址非法");
            return returnObject;
        }
        if(isRemPwd.equals("true")){
            Cookie loginact = new Cookie("loginAct",user.getLoginAct());
            loginact.setMaxAge(60*60*24*10);
            Cookie pwd = new Cookie("loginPwd",user.getLoginPwd());
            pwd.setMaxAge(60*60*24*10);
            response.addCookie(loginact);
            response.addCookie(pwd);
        }
        if(isRemPwd.equals("false")){
            Cookie loginact = new Cookie("loginAct","1");
            loginact.setMaxAge(0);
            Cookie pwd = new Cookie("loginPwd","1");
            pwd.setMaxAge(0);
            response.addCookie(loginact);
            response.addCookie(pwd);
        }
        session.setAttribute(Contants.SESSION_USER,user);
        returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        return returnObject;
    }
    //登出操作
    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession();
        session.invalidate();
        Cookie loginact = new Cookie("loginAct","1");
        loginact.setMaxAge(0);
        Cookie pwd = new Cookie("loginPwd","1");
        pwd.setMaxAge(0);
        response.addCookie(loginact);
        response.addCookie(pwd);
        return "redirect:/";
    }
}
