package cn.xsq.crm.workbench.web.controller;

import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入ActivityController了！");

        String path = request.getServletPath();

        if ("/workbench/activity/findAll.do".equals(path)){

            findAll(request, response);
        }

        if ("/workbench/activity/save.do".equals(path)){
            save(request, response);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
    }

    private void findAll(HttpServletRequest request, HttpServletResponse response) {

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = us.findAll();

        PrintJson.printJsonObj(response , users);
    }
}
