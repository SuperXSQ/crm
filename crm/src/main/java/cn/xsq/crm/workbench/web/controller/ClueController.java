package cn.xsq.crm.workbench.web.controller;

import cn.xsq.crm.exception.DeleteException;
import cn.xsq.crm.exception.SaveException;
import cn.xsq.crm.exception.UpdateException;
import cn.xsq.crm.settings.domain.User;
import cn.xsq.crm.settings.service.UserService;
import cn.xsq.crm.settings.service.impl.UserServiceImpl;
import cn.xsq.crm.utils.DateTimeUtil;
import cn.xsq.crm.utils.PrintJson;
import cn.xsq.crm.utils.ServiceFactory;
import cn.xsq.crm.utils.UUIDUtil;
import cn.xsq.crm.vo.PageVo;
import cn.xsq.crm.workbench.domain.Activity;
import cn.xsq.crm.workbench.domain.ActivityRemark;
import cn.xsq.crm.workbench.service.ActivityService;
import cn.xsq.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入ClueController了！");

        String path = request.getServletPath();

        if ("/workbench/clue/findAll.do".equals(path)){
            findAll(request, response);
        }

    }

    private void findAll(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到findAll，获取所有者列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> users = us.findAll();

        PrintJson.printJsonObj(response, users);
    }

}
