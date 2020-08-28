package cn.xsq.crm.settings.web.controller;

import cn.xsq.crm.exception.LoginFailureException;
import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.MD5Util;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //取得请求路径
        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){

            login(request, response);

        }else if ("/settings/user/save.do".equals(path)){
            //XXX(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到用户控制器UserController了！");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = MD5Util.getMD5(request.getParameter("loginPwd"));
        String ip = request.getRemoteAddr();


        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            //用户名或密码正确
            User user = us.login(loginAct, loginPwd, ip);

            request.getSession().setAttribute("user" , user);

            PrintJson.printJsonFlag(response , true);

        } catch (LoginFailureException e) {

            String msg = e.getMessage();

            Map<String , Object> msgMap = new HashMap<>();
            msgMap.put("success" , false);
            msgMap.put("msg" , msg);
            PrintJson.printJsonObj(response , msgMap);
        }
    }
}
